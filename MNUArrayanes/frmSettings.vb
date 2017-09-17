Imports System.Net
Public Class frmSettings
    Public Refreshing As Boolean = False

#Region "Public Subs"
    ''' <summary>
    ''' Refresh frmSettings connected clients list.
    ''' </summary>
    Public Sub RefreshConnectionsList()
        ' Don't refresh twice
        lstConnections.Items.Clear()
        Refreshing = True

        ' No race conditions. Threads shall wait for this to be updated.
        SyncLock Me

            Dim clientList2 As New Dictionary(Of IPEndPoint, frmPrincipal.incomingClient)(frmPrincipal.listaClientes)
            For Each cliente As KeyValuePair(Of IPEndPoint, frmPrincipal.incomingClient) In frmPrincipal.listaClientes
                Dim info As frmPrincipal.incomingClient = cliente.Value
                Try
                    lstConnections.Items.Add(cliente.Value.DeviceInfo.DeviceID.ToString() & " " & cliente.Value.devState.ToString())
                    clientList2(cliente.Key) = info
                Catch nex As NullReferenceException
                    ' TODO: Throw an error? Use to happen when trying to update and got to the last item of
                    ' the loop. Doesn't happen anymore, yet I kept this for convenience. 
                    Continue For
                End Try
            Next
            frmPrincipal.listaClientes = clientList2
        End SyncLock
        lstConnections.Refresh()
        Refreshing = False
    End Sub

    ''' <summary>
    ''' Update an item in the list. 
    ''' </summary>
    ''' <param name="itemIndex">Index of the item to be modified.</param>
    ''' <param name="text">Text for the item that's being modified.</param>
    Public Sub UpdateList(ByVal itemIndex As Integer, ByVal text As String)
        If lstConnections.Items.Count >= itemIndex And lstConnections.Items.Count <> 0 Then
            lstConnections.Items(itemIndex) = text
        End If
    End Sub

#End Region
#Region "Form Actions"
    Private Sub cmdOK_Click(sender As Object, e As EventArgs) Handles cmdOK.Click
        My.Settings.Save()
        Me.Close()
    End Sub

    Private Sub cmdCancel_Click(sender As Object, e As EventArgs) Handles cmdCancel.Click
        My.Settings.Reset()
        Me.Close()
    End Sub

    Private Sub cmdKick_Click(sender As Object, e As EventArgs) Handles cmdKick.Click

        ' Is anyone selected?
        If lstConnections.SelectedIndex = -1 Then
            Return
        End If

        ' Get his info
        Dim info As frmPrincipal.incomingClient
        For Each cliente As KeyValuePair(Of IPEndPoint, frmPrincipal.incomingClient) In frmPrincipal.listaClientes
            If cliente.Value.listIndex = lstConnections.SelectedIndex Then
                info = cliente.Value
            End If
        Next

        ' Kick him
        Try
            frmPrincipal.KickClient(info)
        Catch Nex As NullReferenceException
            Return
        End Try

        If Not Refreshing Then
            RefreshConnectionsList()
        End If
    End Sub

    Private Sub cmdRefresh_Click(sender As Object, e As EventArgs) Handles cmdRefresh.Click
        If Not Refreshing Then
            RefreshConnectionsList()
        End If
    End Sub

    Private Sub frmSettings_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        ' FIXME: The network adapter may change the address list index of valid IP's. Get the one for our router,
        ' not the ones for virtual adapters (Hamachi / VirtualBox Adapter / Tunngle / etc).
        lblIP.Text = "IP: " & Dns.GetHostByName(Dns.GetHostName()).AddressList(0).ToString()
        lblPort.Text = "Port: 1855"
    End Sub
#End Region
End Class