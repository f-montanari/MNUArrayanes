''' <summary>
''' Holds the info of any Device that connected to the server. If the device is paired correctly, this info
''' is filled.
''' </summary>
Public Class ConnectedDeviceInformation
    Public DeviceID As String
    Public ClientVersion As String
    Public maxVotes As Integer
End Class