Imports System.Threading
Imports System.Net.Sockets
Imports System.Text
Imports System.Collections.Concurrent

''' <summary>
''' Server related class.
''' 
''' Credits to: http://www.elguille.info/colabora/puntoNET/PabloTilli_SocketsVBNET.htm
''' </summary>
Public Class Servidor
#Region "Structures"
    Public Structure InfoDeUnCliente
        Public socket As TcpClient
        Public thread As Thread
        Public datoRecibido As String
    End Structure
#End Region
#Region "Vars"
    Private Started As Boolean = False
    Private tcpLsn As TcpListener
    Private Clientes As New ConcurrentDictionary(Of Net.IPEndPoint, InfoDeUnCliente)
    Private tcpThd As Thread
    Private clientID As Net.IPEndPoint
    Private m_PuertoDeEscucha As String
    Private threads As New List(Of Thread)
#End Region
#Region "Events"
    Public Event NuevaConexion(ByVal IDTerminal As Net.IPEndPoint)
    Public Event DatosRecibidos(ByVal IDTerminal As Net.IPEndPoint)
    Public Event ConexionTerminada(ByVal IDTerminal As Net.IPEndPoint)
#End Region
#Region "Properties"

    Property PuertoDeEscucha() As String
        Get
            PuertoDeEscucha = m_PuertoDeEscucha
        End Get
        Set(ByVal value As String)
            m_PuertoDeEscucha = value
        End Set
    End Property
#End Region
#Region "Methods"
    Public Sub EnviarRespuesta(ByVal IDCliente As Net.IPEndPoint, ByVal message As String)

        ' Evitemos que se haya desconectado el cliente.
        If Not Clientes.ContainsKey(IDCliente) Then

            Return
        End If
        Dim clnReceptor As InfoDeUnCliente = Clientes(IDCliente)
        Dim BufferDeEscritura() As Byte
        Dim diffStream As NetworkStream
        BufferDeEscritura = Encoding.UTF8.GetBytes(message & vbNewLine)
        diffStream = clnReceptor.socket.GetStream()
        If Not (diffStream Is Nothing) Then
            'Envio los datos al Servidor
            diffStream.Write(BufferDeEscritura, 0, BufferDeEscritura.Length)
        Else
            Throw New DataException("No se obtuvo el Stream de datos del Cliente")
        End If
    End Sub

    Public Sub Escuchar()
        tcpLsn = New TcpListener(PuertoDeEscucha)
        tcpLsn.Start()
        Started = True
        tcpThd = New Thread(AddressOf EsperarCliente)
        threads.Add(tcpThd)
        tcpThd.Start()
    End Sub

    Public Function ObtenerDatos(ByVal IDCliente As Net.IPEndPoint) As String
        'Dim InfoClienteSolicitado As InfoDeUnCliente        

        'Obtengo la informacion del cliente solicitado
        SyncLock Me
            Dim InfoCliente As InfoDeUnCliente
            If Clientes.TryGetValue(IDCliente, InfoCliente) Then
                Return Clientes(IDCliente).datoRecibido
            End If
        End SyncLock
    End Function
    Public Sub Cerrar(ByVal closeCallback As Action)
        tcpLsn.Stop()
        Started = False

        For Each cliente As InfoDeUnCliente In Clientes.Values
            cliente.socket.Close()
            cliente.thread.Abort()
            threads.Remove(cliente.thread)
        Next

        closeCallback()
    End Sub
    Public Sub DesconectarCliente(ByVal ipcliente As Net.IPEndPoint)
        Debug.WriteLine("Intentando desconectar " & ipcliente.Address.ToString())
        For Each infoCliente As InfoDeUnCliente In Clientes.Values
            If infoCliente.thread.Name = ipcliente.Address.ToString() & ":" & ipcliente.Port.ToString() Then
                infoCliente.thread.Abort()
                infoCliente.socket.Close()
                threads.Remove(infoCliente.thread)
            End If
        Next
    End Sub
#End Region
#Region "Private Methods"
    Private Sub EsperarCliente()
        Dim InfoClienteActual As New InfoDeUnCliente

        With InfoClienteActual

            While Started
                Try
                    'Cuando se recibe la conexion, guardo la informacion del cliente

                    'Guardo el Socket que utilizo para mantener la conexion con el cliente

                    .socket = tcpLsn.AcceptTcpClient() 'Se queda esperando la conexion de un cliente

                    'Guardo el el RemoteEndPoint, que utilizo para identificar al cliente
                    clientID = .socket.Client.RemoteEndPoint

                    'Creo un Thread para que se encargue de escuchar los mensaje del cliente
                    .thread = New Thread(AddressOf LeerSocket)
                    .thread.Name = clientID.Address.ToString() & ":" & clientID.Port
                    'Agrego la informacion del cliente al HashArray Clientes, donde esta la
                    'informacion de todos estos
                    SyncLock Me
                        Clientes.TryAdd(clientID, InfoClienteActual)
                    End SyncLock

                    'Genero el evento Nueva conexion
                    RaiseEvent NuevaConexion(clientID)

                    threads.Add(.thread)
                    'Inicio el thread encargado de escuchar los mensajes del cliente
                    .thread.Start()
                Catch ex As SocketException
                    ' Deberíamos hacer algo si ocurre esto. No puedo esperar que el SocketException sea sólo porque se cierre una conexión.
                    Return
                End Try
            End While
        End With

    End Sub
    Private Sub LeerSocket()
        Dim IDReal As Net.IPEndPoint 'ID del cliente que se va a escuchar
        Dim Recibir() As Byte 'Array utilizado para recibir los datos que llegan
        Dim InfoClienteActual As InfoDeUnCliente 'Informacion del cliente que se va escuchar
        Dim Ret As Integer = 0

        IDReal = clientID
        InfoClienteActual = Clientes(IDReal)

        With InfoClienteActual
            While True
                If .socket.Connected Then

                    'TODO: Change hardcoded byte size to optimize data usage.
                    Recibir = New Byte(1024) {}

                    Try
                        'Me quedo esperando a que llegue un mensaje desde el cliente
                        Ret = .socket.Client.Receive(Recibir, Recibir.Length, SocketFlags.None)

                        If Ret > 0 Then
                            'Guardo el mensaje recibido
                            .datoRecibido = Encoding.ASCII.GetString(Recibir)
                            Clientes(IDReal) = InfoClienteActual

                            'Genero el evento de la recepcion del mensaje
                            RaiseEvent DatosRecibidos(IDReal)
                        Else
                            'Genero el evento de la finalizacion de la conexion
                            RaiseEvent ConexionTerminada(IDReal)
                            Exit While
                        End If
                        'Catch nullE As NullReferenceException
                        '    Debug.WriteLine("Conexión cerrada")
                    Catch ioe As IO.IOException
                        'Genero el evento de la finalizacion de la conexion
                        Debug.WriteLine("[ERROR] " & ioe.Message)
                        RaiseEvent ConexionTerminada(IDReal)
                        Exit While
                    Catch scktEx As SocketException
                        Debug.WriteLine("[ERROR] Socket exception: " & scktEx.Message)
                        RaiseEvent ConexionTerminada(IDReal)
                        Exit While
                    End Try
                End If
            End While

            Call CerrarThread(IDReal)
        End With
    End Sub

    Private Sub CerrarThread(ByVal IDCliente As Net.IPEndPoint)
        Dim InfoClienteActual As InfoDeUnCliente

        'Cierro el thread que se encargaba de escuchar al cliente especificado
        InfoClienteActual = Clientes(IDCliente)

        Try
            InfoClienteActual.thread.Abort()
        Catch e As Exception
            Debug.WriteLine("Error cerrando thread: " & e.Message)
        Finally
            SyncLock Me
                'Elimino el cliente del HashArray que guarda la informacion de los clientes
                Clientes.TryRemove(IDCliente, InfoClienteActual)
            End SyncLock
        End Try
    End Sub
#End Region
End Class
