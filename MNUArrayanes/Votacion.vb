''' <summary>
''' Votation data sent from the device.
''' </summary>
Public Class Votacion
    Public AFavor As Integer
    Public EnContra As Integer
    Public Abstenciones As Integer

    Public Overrides Function ToString() As String
        Return "Votación:" & vbNewLine &
               "A Favor:" & AFavor.ToString() & vbNewLine &
               "En Contra:" & EnContra.ToString() & vbNewLine &
               "Abstenciones:" & Abstenciones.ToString()
    End Function
End Class
