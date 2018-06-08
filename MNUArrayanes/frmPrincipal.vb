Imports System.Net
Imports System.IO
Imports Newtonsoft.Json

Public Class frmPrincipal

    ''' <summary>
    ''' Enum for the different states of the device.
    '''   * CONNECTED = The device hasn't sent it's info yet. We don't know who it is.
    '''   * PAIRED = The device has identified itself. In this state it can send info or request it.
    '''   * DISCONNECTED = Not being used yet. Should be implemented in a way that the system keeps record of 
    ''' who connected, remember it, and wait for it's return.
    '''   * SEND_DATA = The device is sending current voting data. After data is sent, 
    ''' we pair it and the device is switched again to <see cref="DeviceState.PAIRED"/>
    '''   * GET_INFO = The device wants new voting info. Important data should be sent in this state so the 
    ''' device knows what to expect. Up to now, only sends confirmation to the device.
    ''' </summary>
    Public Enum DeviceState
        CONNECTED = 0
        PAIRED = 1
        DISCONNECTED = 2
        SEND_DATA = 3
        GET_INFO = 4
    End Enum

    ''' <summary>
    ''' Structure that holds important client data, such as IP Address, Pairing State, and others.
    ''' </summary>
    Public Structure incomingClient
        Public IP As IPEndPoint
        Public devState As DeviceState
        Public DeviceInfo As ConnectedDeviceInformation
        Public listIndex As Integer
    End Structure

    ' Client dictionary. Maybe we could make a static class for this kind of data?
    Public listaClientes As New Dictionary(Of IPEndPoint, incomingClient)

    Private isVoting As Boolean = False
    Private WithEvents srvInstance As New Servidor()
    Private intVotados As Integer = 0
    Private intDeviceCount As Integer = 0
    Private intAprobados As Integer = 0
    Private intDesaprobados As Integer = 0
    Private intCantVotantes As Integer = 0

    ' Private fntDelegaciones, fntVotantes, fntResultados As Font

    ''' <summary>
    ''' This gets called when Server.Close() is called.
    ''' It's done that way in order to prevent the app from exiting before actually closing
    ''' all connections.
    ''' </summary>
    Private CloseAction As New Action(Sub()
                                          Application.Exit()
                                      End Sub)

#Region "Public Methods"

    ''' <summary>
    ''' Use for displaying errors in the console AND to write it in the log file.
    ''' </summary>
    ''' <param name="message">The message to be written into the log file.</param>
    Public Sub Log(ByVal message As String)
        Debug.WriteLine(message)
        Try
            Dim writer As TextWriter = File.AppendText(Application.StartupPath & "/server.log")
            writer.WriteLine(message)
            writer.Close()
        Catch ioEx As IOException
            Return
        End Try
    End Sub

    ''' <summary>
    ''' Disconnects a client from the platform, and adjusts data taking this into account.
    ''' </summary>
    ''' <param name="infoCliente">The client to be disconnected.</param>
    Public Sub KickClient(ByVal infoCliente As incomingClient)
        intCantVotantes -= infoCliente.DeviceInfo.maxVotes
        Invoke(Sub()
                   txtNumDelegaciones.Text = intCantVotantes.ToString()
               End Sub)
        If isVoting Then

            ' Reset votation
            isVoting = False
            ResetVotationData()
            Invoke(Sub()
                       cmdNuevo.Enabled = True
                       lblStatus.Text = "Listo."
                   End Sub)
        End If
        srvInstance.DesconectarCliente(infoCliente.IP)
        listaClientes.Remove(infoCliente.IP)
    End Sub
#End Region

#Region "Private Methods"
    ''' <summary>
    ''' Disconnect a client that didn't pass the connection pairing.
    ''' </summary>
    ''' <param name="infoCliente">The client to disconnect.</param>
    Private Sub DisconnectClient(ByVal infoCliente As incomingClient)
        srvInstance.EnviarRespuesta(infoCliente.IP, "Error parsing data, invalid connection")
        intCantVotantes -= infoCliente.DeviceInfo.maxVotes
        cntAFavor.MaxValue = intCantVotantes
        cntEnContra.MaxValue = intCantVotantes

        listaClientes.Remove(infoCliente.IP)
        srvInstance.DesconectarCliente(infoCliente.IP)
    End Sub

    ''' <summary>
    ''' Resets all data that involves the voting, such as the results and the devices that
    ''' sent their data.
    ''' </summary>
    Private Sub ResetVotationData()
        intDeviceCount = 0
        intVotados = 0
        intAprobados = 0
        intDesaprobados = 0

        cntAFavor.SetCounter(intAprobados)
        cntEnContra.SetCounter(intDesaprobados)
        txtResultado.Text = ""

        tmrCleanUI.Enabled = False
    End Sub

    ''' <summary>
    ''' Show voting results into screen. Use only when data has finished arriving.
    ''' </summary>
    Private Sub ShowVotaciones()
        lblStatus.Text = "Listo."

        cntAFavor.SetCounter(intAprobados)
        cntEnContra.SetCounter(intDesaprobados)

        If intAprobados > intDesaprobados Then
            txtResultado.Text = "APROBADA"
        ElseIf intAprobados < intDesaprobados Then
            txtResultado.Text = "RECHAZADA"
        ElseIf intAprobados = intDesaprobados Then
            txtResultado.Text = "EMPATE"
        End If

        tmrCleanUI.Enabled = True
    End Sub

    ''' <summary>
    ''' Set client's pairing state to desired state. 
    ''' Also refreshes the device list with the new data.
    ''' </summary>
    ''' <param name="clientInfo">The client whose state is going to be changed.</param>
    ''' <param name="state">The desired state.</param>
    Private Sub SetDeviceState(ByRef clientInfo As incomingClient, ByVal state As DeviceState)
        clientInfo.devState = state

        Dim client = clientInfo
        Invoke(Sub()
                   frmSettings.UpdateList(client.listIndex, client.DeviceInfo.DeviceID & " - " & client.devState.ToString())
               End Sub)
    End Sub

    ''' <summary>
    ''' Pairs incoming voting data and adds it to the current voting state.
    ''' If the voting data isn't valid, it won't be taken into account.
    ''' </summary>
    ''' <param name="clientInfo">The client that sent the voting data.</param>
    ''' <param name="data">The data to be paired.</param>
    Private Sub PairVotaciones(ByRef clientInfo As incomingClient, ByVal data As String)

        Dim votacionEntrante As Votacion

        ' JSON Deserialization.
        Try
            votacionEntrante = JsonConvert.DeserializeObject(Of Votacion)(data)
        Catch jsonEx As JsonException
            ' Should stop voting if this happens.
            Log("[JSON ERROR]" & jsonEx.Message)
            Return
        End Try

        Log("Votacion Entrante:")
        Log(votacionEntrante.ToString())

        SetDeviceState(clientInfo, DeviceState.PAIRED)

        ' Update voting information.
        intAprobados += votacionEntrante.AFavor
        intDesaprobados += votacionEntrante.EnContra
        intVotados += 1
        Invoke(Sub()
                   lblStatus.Text = "Esperando resultados (" & intVotados & "/" & intDeviceCount & ")"
                   If intVotados = intDeviceCount Then
                       ShowVotaciones()
                       isVoting = False
                       cmdNuevo.Enabled = True
                   End If
               End Sub)
    End Sub

    ''' <summary>
    ''' Pairs incoming client data.
    ''' If data is valid, it updates voting data, set's client's state to paired, and lets the client know that it's successfully
    ''' paired.
    ''' </summary>
    ''' <param name="data">The data to be paired. 
    ''' Has to be in JSON format, in order to be serialized into a <see cref="ConnectedDeviceInformation"/> via Newtonsoft
    ''' jsonConvert.</param>
    ''' <param name="infoCliente">Client who sent the data.</param>
    Private Sub PairClientData(ByVal data As String, ByRef infoCliente As incomingClient)
        Log("[Server] Pairing client data")
        Try
            Dim deviceInfo As ConnectedDeviceInformation = JsonConvert.DeserializeObject(Of ConnectedDeviceInformation)(data)
            Log("Hello " & deviceInfo.DeviceID)

            ' TODO: Add version control / any kind of data validation.
            If Not deviceInfo.ClientVersion = "v0.1" Then
                Log("Outdated version")
            Else
                Log("Current version is OK")
            End If

            ' Add new voting members.
            intCantVotantes += deviceInfo.maxVotes

            Invoke(Sub()
                       txtNumDelegaciones.Text = intCantVotantes.ToString()
                       cntAFavor.MaxValue = intCantVotantes
                       cntEnContra.MaxValue = intCantVotantes
                   End Sub)

            ' Update client info with paired data.
            infoCliente.devState = DeviceState.PAIRED
            infoCliente.DeviceInfo = deviceInfo
            srvInstance.EnviarRespuesta(infoCliente.IP, "device_paired")

            Dim client = infoCliente
            Invoke((Sub()
                        frmSettings.UpdateList(client.listIndex, client.DeviceInfo.DeviceID & " - " & client.devState.ToString())
                    End Sub))

        Catch serializationEx As JsonSerializationException
            Log("[JSON ERROR] " & serializationEx.Message)
            DisconnectClient(infoCliente)
        Catch jsonEx As JsonException
            Log("[JSON ERROR] " & jsonEx.Message)
            DisconnectClient(infoCliente)
        Catch ex As Exception
            Log("[ERROR] Error paireando la info: " & ex.Message)
        End Try
    End Sub
#End Region

#Region "Server Actions"

    ''' <summary>
    ''' New connection handler. Handle new connections here.
    ''' </summary>
    ''' <param name="IDTerminal">The <see cref="IPEndPoint"/> of the client that connected.</param>
    Private Sub srvInstance_NuevaConexion(IDTerminal As IPEndPoint) Handles srvInstance.NuevaConexion
        Log("[Server] Conexión entrante:" & IDTerminal.Address.ToString() & ":" & IDTerminal.Port.ToString())

        Dim cliente As New incomingClient
        Dim info As New ConnectedDeviceInformation()

        info.DeviceID = "Incoming client"
        info.ClientVersion = "?"

        ' TODO: Is this client already in our list? Check for repeating clients / duplicates.
        If listaClientes.ContainsKey(IDTerminal) Then
            ' This guy is already connected, don't do nothing. 
            Return
        End If


        cliente.DeviceInfo = info
        cliente.IP = IDTerminal
        cliente.devState = DeviceState.CONNECTED

        ' Now we've got the client that connected, get him to the list.
        listaClientes.Add(IDTerminal, cliente)

        Invoke((Sub()
                    frmSettings.RefreshConnectionsList()
                End Sub))
    End Sub

    ''' <summary>
    ''' This event is triggered when data is received. Device data handling goes in here.
    ''' </summary>
    ''' <param name="IDTerminal">The <see cref="IPEndPoint"/> of the device that sent the data.</param>
    Private Sub srvInstance_DatosRecibidos(IDTerminal As IPEndPoint) Handles srvInstance.DatosRecibidos
        Dim data As String = srvInstance.ObtenerDatos(IDTerminal)

        ' Remove empty space
        If Not data Is Nothing Then
            data = data.Trim(New Char() {vbNullChar})
        Else
            Log("[ERROR] Received empty data. Is this an error from the server or the client?")
            Return
        End If

        'Is it pinging?
        If data = "ping" Then
            srvInstance.EnviarRespuesta(IDTerminal, "pong")
        End If
        If data = "pong" Then
            'We pinged. 
        End If
        ' TODO: Remove debugging line
        Log("[Server] Info recibida de " & IDTerminal.ToString() & " (" & listaClientes(IDTerminal).devState & "): " & data & vbNewLine)

        ' Handler of possible data received from the device, defined by it's state. 
        Select Case listaClientes(IDTerminal).devState
            Case DeviceState.CONNECTED
                PairClientData(data, listaClientes(IDTerminal))

            Case DeviceState.PAIRED
                ' Change state if needed.
                With data
                    If .Contains("get_data") Then
                        SetDeviceState(listaClientes(IDTerminal), DeviceState.SEND_DATA)
                        srvInstance.EnviarRespuesta(IDTerminal, "send_data")
                    End If
                End With

            Case DeviceState.SEND_DATA
                PairVotaciones(listaClientes(IDTerminal), data)

            Case DeviceState.GET_INFO

                ' We should get this when we do new votes, so we send number of delegations and change it to paired
                If data.Contains("send_info") Then
                    Log("[Server] Sending info to " & IDTerminal.Address.ToString & ":" & IDTerminal.Port.ToString() &
                        "(" & listaClientes(IDTerminal).DeviceInfo.DeviceID & "): " & listaClientes(IDTerminal).DeviceInfo.maxVotes)
                    srvInstance.EnviarRespuesta(IDTerminal, listaClientes(IDTerminal).DeviceInfo.maxVotes)
                    SetDeviceState(listaClientes(IDTerminal), DeviceState.PAIRED)
                End If
            Case Else
                ' What?
                Log("[???] Incoming from " & listaClientes(IDTerminal).DeviceInfo.DeviceID & " with unknown state: " & data)
        End Select
    End Sub

    ''' <summary>
    ''' This event is triggered when a device disconnected. Handle anything related to disconnected clients here.
    ''' </summary>
    ''' <param name="IDTerminal">The <see cref="IPEndPoint"/> of the device that disconnected.</param>
    Private Sub srvInstance_ConexionTerminada(IDTerminal As IPEndPoint) Handles srvInstance.ConexionTerminada
        Try
            intCantVotantes -= listaClientes(IDTerminal).DeviceInfo.maxVotes
            Invoke(Sub()
                       txtNumDelegaciones.Text = intCantVotantes.ToString()
                       cntAFavor.MaxValue = intCantVotantes
                       cntEnContra.MaxValue = intCantVotantes
                   End Sub)
            If isVoting Then
                ' Tell anyone that there's been a disconnection.
                MsgBox("Un dispositivo se ha desconectado!" & vbNewLine & "ID: " & listaClientes(IDTerminal).DeviceInfo.DeviceID, vbOKOnly + vbExclamation, "Atención!")
                ' Are we left without any device?
                ' Reset votation if we don't have any more votations
                isVoting = False
                ResetVotationData()
                Invoke(Sub()
                           cmdNuevo.Enabled = True
                           lblStatus.Text = "Listo."
                       End Sub)
            End If
            Log("[Server] Conexión terminada: " & IDTerminal.Address.ToString())
        Catch gex As KeyNotFoundException
            Log("[Error] Device not found.")
        Catch ex As Exception
            Log("[Error] Error cerrando conexión: " & ex.Message)
        End Try

        ' Remove the device from our list.
        ' TODO: Maybe we should just leave him as disconnected, and check if it reconnects.
        listaClientes.Remove(IDTerminal)

        Try
            Invoke(Sub()
                       frmSettings.RefreshConnectionsList()
                   End Sub)
        Catch ex As Exception
            MsgBox(ex.Message)
        End Try
    End Sub
#End Region

#Region "Form Actions"

    Private Sub cmdNuevo_Click(sender As Object, e As EventArgs) Handles cmdNuevo.Click

        ' Init votation.
        Try
            ' Anyone connected?
            If listaClientes.Count = 0 Then
                MsgBox("No hay clientes conectados al sistema." & vbNewLine &
                       "Por favor, asegúrese que todas las aplicaciones se hayan conectado al sistema correctamente.",
                       vbExclamation + vbOKOnly, "Atención")
                Return
            End If

            If Convert.ToInt16(txtNumDelegaciones.Text) > 0 Then

                ResetVotationData()
                tmrCleanUI.Enabled = False
                Dim lstClients As New List(Of incomingClient)((listaClientes.Values))

                ' Keep track of how many we were when the votation started.
                intDeviceCount = lstClients.Count
                For Each dev As incomingClient In lstClients
                    srvInstance.EnviarRespuesta(dev.IP, "get_info")
                    SetDeviceState(listaClientes(dev.IP), DeviceState.GET_INFO)
                Next
                lblStatus.Text = "Esperando resultados (" & intVotados & "/" & intDeviceCount & ")"
                isVoting = True
                cmdNuevo.Enabled = False
            End If
        Catch fex As FormatException
            ' We got this error when trying to parse txtNumDelegaciones's Text. Altered data.
            Log("[ERROR] Format exception:" & fex.Message)
            Return
        End Try
    End Sub

    Private Sub frmPrincipal_FormClosing(sender As Object, e As FormClosingEventArgs) Handles MyBase.FormClosing
        srvInstance.Cerrar(CloseAction)
    End Sub

    Private Sub frmPrincipal_Load(sender As Object, e As EventArgs) Handles MyBase.Load

        cntAFavor.ControlColor = Color.Lime
        cntEnContra.ControlColor = Color.Red


        If My.Settings.LogFile Then
            If Not File.Exists(Application.StartupPath & "/server.log") Then
                File.Create(Application.StartupPath & "/server.log").Close()
            End If
        End If

        ' TODO: Make a class for frmSettings to handle the data correctly?
        frmSettings.Show()
        frmSettings.Hide()

        ResetVotationData()

        ' FIXME: Hardcoded port.
        srvInstance.PuertoDeEscucha = 1855
        Log("[Server] Trying to start server at port 1855, IP:" & Dns.GetHostByName(Dns.GetHostName()).AddressList(0).ToString())
        Try
            ' Init server.
            srvInstance.Escuchar()
            lblStatus.Text = "Listo."
            Log("[Server] Ready")
        Catch ex As Exception
            MsgBox("Error iniciando servidor: " & ex.Message, vbOKOnly + vbCritical, "Error")
            Log("[ERROR] Error iniciando servidor" & ex.Message)
            Application.Exit()
        End Try
    End Sub

    Private Sub cmdOpciones_Click(sender As Object, e As EventArgs) Handles cmdOpciones.Click
        frmSettings.Show()
        frmSettings.RefreshConnectionsList()
    End Sub


    Private Sub tmrCleanUI_Tick(sender As Object, e As EventArgs) Handles tmrCleanUI.Tick
        ResetVotationData()
    End Sub

    Private Sub frmPrincipal_ResizeEnd(sender As Object, e As EventArgs) Handles Me.ResizeEnd
        cntAFavor.SetCounter(intAprobados)
        cntEnContra.SetCounter(intDesaprobados)
    End Sub

#End Region

End Class
