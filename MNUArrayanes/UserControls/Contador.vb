Imports System.ComponentModel

Public Class Contador

    Public Value As Integer = 0
    Public MinValue As Integer = 0
    Public MaxValue As Integer = 100
    Private bgColor As Color = Color.Gray
    Private frontColor As Color = Color.Red

    Private timeElapsed As Single = 0
    Private totalTime As Single = 100

    <Description("Background color of the counter"), Category("Appearance")>
    Public Property CounterBackColor() As Color
        Get
            Return bgColor
        End Get
        Set(value As Color)
            bgColor = value
        End Set
    End Property

    <Description("Front color of the counter"), Category("Appearance")>
    Public Property ControlColor() As Color
        Get
            Return frontColor
        End Get
        Set(value As Color)
            frontColor = value
        End Set
    End Property

    Private Sub Panel1_Paint(sender As Object, e As PaintEventArgs) Handles Panel1.Paint
        e.Graphics.Clear(Color.White)
        e.Graphics.SmoothingMode = Drawing2D.SmoothingMode.AntiAlias

        Dim rect As Rectangle = New Rectangle(10, 10,
        Me.ClientSize.Width - 20,
        Me.ClientSize.Height - 20)

        Dim thin_pen As Pen = New Pen(CounterBackColor, 10)
        e.Graphics.DrawArc(thin_pen, rect, -180, 360)
        Dim thick_pen As Pen = New Pen(ControlColor, 10)
        Dim counterAngle As Single = 360 * Value / MaxValue
        e.Graphics.DrawArc(thick_pen, rect, -180, counterAngle * timeElapsed / totalTime)
    End Sub

    Public Sub SetCounter(ByVal value As Integer)
        Me.Value = value
        lblValue.Text = value.ToString()
        tmrAnimation.Enabled = True
    End Sub

    Private Sub tmrAnimation_Tick(sender As Object, e As EventArgs) Handles tmrAnimation.Tick
        timeElapsed += 1
        If timeElapsed >= totalTime Then
            timeElapsed = 0
            tmrAnimation.Enabled = False
        Else
            Refresh()
        End If
    End Sub
End Class
