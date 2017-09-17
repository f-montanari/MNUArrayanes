<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class frmPrincipal
    Inherits System.Windows.Forms.Form

    'Form reemplaza a Dispose para limpiar la lista de componentes.
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
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(frmPrincipal))
        Me.txtResultado = New System.Windows.Forms.TextBox()
        Me.cmdOpciones = New System.Windows.Forms.Button()
        Me.cmdNuevo = New System.Windows.Forms.Button()
        Me.lblStatus = New System.Windows.Forms.TextBox()
        Me.TableLayoutPanel1 = New System.Windows.Forms.TableLayoutPanel()
        Me.PictureBox5 = New System.Windows.Forms.PictureBox()
        Me.PictureBox6 = New System.Windows.Forms.PictureBox()
        Me.PictureBox7 = New System.Windows.Forms.PictureBox()
        Me.Panel1 = New System.Windows.Forms.Panel()
        Me.txtNumAprobados = New System.Windows.Forms.Label()
        Me.Panel2 = New System.Windows.Forms.Panel()
        Me.txtNumDesaprobados = New System.Windows.Forms.Label()
        Me.Panel3 = New System.Windows.Forms.Panel()
        Me.txtNumDelegaciones = New System.Windows.Forms.Label()
        Me.PictureBox1 = New System.Windows.Forms.PictureBox()
        Me.TableLayoutPanel2 = New System.Windows.Forms.TableLayoutPanel()
        Me.tmrPing = New System.Windows.Forms.Timer(Me.components)
        Me.TableLayoutPanel1.SuspendLayout()
        CType(Me.PictureBox5, System.ComponentModel.ISupportInitialize).BeginInit()
        CType(Me.PictureBox6, System.ComponentModel.ISupportInitialize).BeginInit()
        CType(Me.PictureBox7, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.Panel1.SuspendLayout()
        Me.Panel2.SuspendLayout()
        Me.Panel3.SuspendLayout()
        CType(Me.PictureBox1, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.TableLayoutPanel2.SuspendLayout()
        Me.SuspendLayout()
        '
        'txtResultado
        '
        resources.ApplyResources(Me.txtResultado, "txtResultado")
        Me.txtResultado.BackColor = System.Drawing.SystemColors.Window
        Me.txtResultado.BorderStyle = System.Windows.Forms.BorderStyle.None
        Me.txtResultado.Name = "txtResultado"
        Me.txtResultado.ReadOnly = True
        Me.txtResultado.TabStop = False
        '
        'cmdOpciones
        '
        resources.ApplyResources(Me.cmdOpciones, "cmdOpciones")
        Me.cmdOpciones.Name = "cmdOpciones"
        Me.cmdOpciones.UseVisualStyleBackColor = True
        '
        'cmdNuevo
        '
        resources.ApplyResources(Me.cmdNuevo, "cmdNuevo")
        Me.cmdNuevo.Name = "cmdNuevo"
        Me.cmdNuevo.UseVisualStyleBackColor = True
        '
        'lblStatus
        '
        resources.ApplyResources(Me.lblStatus, "lblStatus")
        Me.lblStatus.BackColor = System.Drawing.SystemColors.Window
        Me.lblStatus.BorderStyle = System.Windows.Forms.BorderStyle.None
        Me.lblStatus.Cursor = System.Windows.Forms.Cursors.Default
        Me.lblStatus.Name = "lblStatus"
        Me.lblStatus.ReadOnly = True
        Me.lblStatus.TabStop = False
        '
        'TableLayoutPanel1
        '
        resources.ApplyResources(Me.TableLayoutPanel1, "TableLayoutPanel1")
        Me.TableLayoutPanel1.Controls.Add(Me.txtResultado, 1, 1)
        Me.TableLayoutPanel1.Controls.Add(Me.PictureBox5, 0, 2)
        Me.TableLayoutPanel1.Controls.Add(Me.PictureBox6, 2, 2)
        Me.TableLayoutPanel1.Controls.Add(Me.PictureBox7, 1, 0)
        Me.TableLayoutPanel1.Controls.Add(Me.Panel1, 0, 1)
        Me.TableLayoutPanel1.Controls.Add(Me.Panel2, 2, 1)
        Me.TableLayoutPanel1.Controls.Add(Me.Panel3, 1, 2)
        Me.TableLayoutPanel1.Controls.Add(Me.PictureBox1, 1, 3)
        Me.TableLayoutPanel1.Controls.Add(Me.TableLayoutPanel2, 2, 4)
        Me.TableLayoutPanel1.Controls.Add(Me.lblStatus, 0, 4)
        Me.TableLayoutPanel1.Name = "TableLayoutPanel1"
        '
        'PictureBox5
        '
        resources.ApplyResources(Me.PictureBox5, "PictureBox5")
        Me.PictureBox5.Image = Global.mnuarrayanes.My.Resources.Resources.A_Favor
        Me.PictureBox5.Name = "PictureBox5"
        Me.PictureBox5.TabStop = False
        '
        'PictureBox6
        '
        resources.ApplyResources(Me.PictureBox6, "PictureBox6")
        Me.PictureBox6.Image = Global.mnuarrayanes.My.Resources.Resources.En_Contra
        Me.PictureBox6.Name = "PictureBox6"
        Me.PictureBox6.TabStop = False
        '
        'PictureBox7
        '
        resources.ApplyResources(Me.PictureBox7, "PictureBox7")
        Me.PictureBox7.Image = Global.mnuarrayanes.My.Resources.Resources.Resultado
        Me.PictureBox7.Name = "PictureBox7"
        Me.PictureBox7.TabStop = False
        '
        'Panel1
        '
        Me.Panel1.BackgroundImage = Global.mnuarrayanes.My.Resources.Resources.OK
        resources.ApplyResources(Me.Panel1, "Panel1")
        Me.Panel1.Controls.Add(Me.txtNumAprobados)
        Me.Panel1.Name = "Panel1"
        '
        'txtNumAprobados
        '
        resources.ApplyResources(Me.txtNumAprobados, "txtNumAprobados")
        Me.txtNumAprobados.BackColor = System.Drawing.Color.FromArgb(CType(CType(0, Byte), Integer), CType(CType(168, Byte), Integer), CType(CType(90, Byte), Integer))
        Me.txtNumAprobados.ForeColor = System.Drawing.SystemColors.Window
        Me.txtNumAprobados.Name = "txtNumAprobados"
        '
        'Panel2
        '
        Me.Panel2.BackgroundImage = Global.mnuarrayanes.My.Resources.Resources.Cancel
        resources.ApplyResources(Me.Panel2, "Panel2")
        Me.Panel2.Controls.Add(Me.txtNumDesaprobados)
        Me.Panel2.Name = "Panel2"
        '
        'txtNumDesaprobados
        '
        resources.ApplyResources(Me.txtNumDesaprobados, "txtNumDesaprobados")
        Me.txtNumDesaprobados.BackColor = System.Drawing.Color.FromArgb(CType(CType(236, Byte), Integer), CType(CType(50, Byte), Integer), CType(CType(55, Byte), Integer))
        Me.txtNumDesaprobados.ForeColor = System.Drawing.SystemColors.Window
        Me.txtNumDesaprobados.Name = "txtNumDesaprobados"
        '
        'Panel3
        '
        resources.ApplyResources(Me.Panel3, "Panel3")
        Me.Panel3.BackgroundImage = Global.mnuarrayanes.My.Resources.Resources.Presentes
        Me.Panel3.Controls.Add(Me.txtNumDelegaciones)
        Me.Panel3.Name = "Panel3"
        '
        'txtNumDelegaciones
        '
        resources.ApplyResources(Me.txtNumDelegaciones, "txtNumDelegaciones")
        Me.txtNumDelegaciones.BackColor = System.Drawing.Color.FromArgb(CType(CType(62, Byte), Integer), CType(CType(64, Byte), Integer), CType(CType(149, Byte), Integer))
        Me.txtNumDelegaciones.ForeColor = System.Drawing.SystemColors.Window
        Me.txtNumDelegaciones.Name = "txtNumDelegaciones"
        '
        'PictureBox1
        '
        resources.ApplyResources(Me.PictureBox1, "PictureBox1")
        Me.PictureBox1.Image = Global.mnuarrayanes.My.Resources.Resources.Presentes_txt
        Me.PictureBox1.Name = "PictureBox1"
        Me.PictureBox1.TabStop = False
        '
        'TableLayoutPanel2
        '
        resources.ApplyResources(Me.TableLayoutPanel2, "TableLayoutPanel2")
        Me.TableLayoutPanel2.Controls.Add(Me.cmdNuevo, 0, 0)
        Me.TableLayoutPanel2.Controls.Add(Me.cmdOpciones, 1, 0)
        Me.TableLayoutPanel2.Name = "TableLayoutPanel2"
        '
        'tmrPing
        '
        Me.tmrPing.Enabled = True
        Me.tmrPing.Interval = 1000
        '
        'frmPrincipal
        '
        resources.ApplyResources(Me, "$this")
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.BackColor = System.Drawing.SystemColors.Window
        Me.Controls.Add(Me.TableLayoutPanel1)
        Me.Name = "frmPrincipal"
        Me.ShowIcon = False
        Me.TableLayoutPanel1.ResumeLayout(False)
        Me.TableLayoutPanel1.PerformLayout()
        CType(Me.PictureBox5, System.ComponentModel.ISupportInitialize).EndInit()
        CType(Me.PictureBox6, System.ComponentModel.ISupportInitialize).EndInit()
        CType(Me.PictureBox7, System.ComponentModel.ISupportInitialize).EndInit()
        Me.Panel1.ResumeLayout(False)
        Me.Panel2.ResumeLayout(False)
        Me.Panel3.ResumeLayout(False)
        CType(Me.PictureBox1, System.ComponentModel.ISupportInitialize).EndInit()
        Me.TableLayoutPanel2.ResumeLayout(False)
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents txtResultado As TextBox
    Friend WithEvents cmdOpciones As Button
    Friend WithEvents cmdNuevo As Button
    Friend WithEvents lblStatus As TextBox
    Friend WithEvents TableLayoutPanel1 As TableLayoutPanel
    Friend WithEvents TableLayoutPanel2 As TableLayoutPanel
    Friend WithEvents PictureBox5 As PictureBox
    Friend WithEvents PictureBox6 As PictureBox
    Friend WithEvents PictureBox7 As PictureBox
    Friend WithEvents Panel3 As Panel
    Friend WithEvents PictureBox1 As PictureBox
    Friend WithEvents txtNumDelegaciones As Label
    Friend WithEvents Panel1 As Panel
    Friend WithEvents Panel2 As Panel
    Friend WithEvents txtNumAprobados As Label
    Friend WithEvents txtNumDesaprobados As Label
    Friend WithEvents tmrPing As Timer
End Class
