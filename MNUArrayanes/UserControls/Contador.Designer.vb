<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class Contador
    Inherits System.Windows.Forms.UserControl

    'UserControl reemplaza a Dispose para limpiar la lista de componentes.
    <System.Diagnostics.DebuggerNonUserCode()>
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Requerido por el Diseñador de Windows Forms
    Private components As System.ComponentModel.IContainer

    'NOTA: el Diseñador de Windows Forms necesita el siguiente procedimiento
    'Se puede modificar usando el Diseñador de Windows Forms.  
    'No lo modifique con el editor de código.
    <System.Diagnostics.DebuggerStepThrough()>
    Private Sub InitializeComponent()
        Me.components = New System.ComponentModel.Container()
        Me.tmrAnimation = New System.Windows.Forms.Timer(Me.components)
        Me.lblValue = New System.Windows.Forms.Label()
        Me.SuspendLayout()
        '
        'tmrAnimation
        '
        Me.tmrAnimation.Interval = 10
        '
        'lblValue
        '
        Me.lblValue.Font = New System.Drawing.Font("Monospac821 BT", 36.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblValue.Location = New System.Drawing.Point(105, 97)
        Me.lblValue.Name = "lblValue"
        Me.lblValue.Size = New System.Drawing.Size(83, 57)
        Me.lblValue.TabIndex = 5
        Me.lblValue.Text = "00"
        Me.lblValue.TextAlign = System.Drawing.ContentAlignment.MiddleCenter
        '
        'Contador
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.Controls.Add(Me.lblValue)
        Me.Name = "Contador"
        Me.Size = New System.Drawing.Size(300, 250)
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents tmrAnimation As Timer
    Friend WithEvents lblValue As Label
End Class
