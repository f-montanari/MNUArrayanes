Imports System.ComponentModel

Public Class Contador

    Public Value As Integer = 0
    Public MinValue As Integer = 0
    Public MaxValue As Integer = 100
    Private bgColor As Color = Color.Gray
    Private frontColor As Color = Color.Red
    Private bufferedGraphics As BufferedGraphics

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



    Private Sub Panel1_Paint(sender As Object, e As PaintEventArgs) Handles MyBase.Paint
        PaintMe()
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
            PaintMe()
        End If
    End Sub

    Private Sub Contador_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        CenterLabel()

        Me.DoubleBuffered = True
        Dim context As BufferedGraphicsContext = BufferedGraphicsManager.Current
        context.MaximumBuffer = New Size(Me.Width + 1, Me.Height + 1)
        bufferedGraphics = context.Allocate(Me.CreateGraphics(), New Rectangle(0, 0, Me.Width, Me.Height))

        PaintMe()
    End Sub

    Private Sub lblValue_Resize(sender As Object, e As EventArgs)
        CenterLabel()
    End Sub

    Private Sub CenterLabel()
        Dim x As Integer = (Me.Width / 2) - (lblValue.Width / 2)
        Dim y As Integer = (Me.Height / 2) - (lblValue.Height / 2)

        lblValue.Location = New Point(x, y)
    End Sub

    Private Sub PaintMe()
        If Not IsNothing(Graphics.FromHwnd(Me.Handle)) Then
            Dim g As Graphics = bufferedGraphics.Graphics
            g.Clear(Color.White)
            Dim rect As Rectangle = New Rectangle(10, 10,
            ClientSize.Width - 20,
            ClientSize.Height - 20)

            Dim thin_pen As Pen = New Pen(CounterBackColor, 10)
            g.DrawArc(thin_pen, rect, -180, 360)

            Dim thick_pen As Pen = New Pen(ControlColor, 10)
            Dim counterAngle As Single = 360 * Value / MaxValue
            g.DrawArc(thick_pen, rect, -180, counterAngle * timeElapsed / totalTime)

            bufferedGraphics.Render(Graphics.FromHwnd(Me.Handle))
        End If
    End Sub

    Private Sub Contador_Resize(sender As Object, e As EventArgs) Handles Me.Resize
        CenterLabel()
        PaintMe()
    End Sub
End Class
