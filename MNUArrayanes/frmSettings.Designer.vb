<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class frmSettings
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
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

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()>
    Private Sub InitializeComponent()
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(frmSettings))
        Me.TableLayoutPanel1 = New System.Windows.Forms.TableLayoutPanel()
        Me.cmdCancel = New System.Windows.Forms.Button()
        Me.cmdOK = New System.Windows.Forms.Button()
        Me.chkLog = New System.Windows.Forms.CheckBox()
        Me.lstConnections = New System.Windows.Forms.ListBox()
        Me.TableLayoutPanel2 = New System.Windows.Forms.TableLayoutPanel()
        Me.TableLayoutPanel3 = New System.Windows.Forms.TableLayoutPanel()
        Me.cmdKick = New System.Windows.Forms.Button()
        Me.cmdReset = New System.Windows.Forms.Button()
        Me.cmdRefresh = New System.Windows.Forms.Button()
        Me.TableLayoutPanel4 = New System.Windows.Forms.TableLayoutPanel()
        Me.lblIP = New System.Windows.Forms.Label()
        Me.lblPort = New System.Windows.Forms.Label()
        Me.Label1 = New System.Windows.Forms.Label()
        Me.Panel1 = New System.Windows.Forms.Panel()
        Me.TableLayoutPanel1.SuspendLayout()
        Me.TableLayoutPanel2.SuspendLayout()
        Me.TableLayoutPanel3.SuspendLayout()
        Me.TableLayoutPanel4.SuspendLayout()
        Me.Panel1.SuspendLayout()
        Me.SuspendLayout()
        '
        'TableLayoutPanel1
        '
        Me.TableLayoutPanel1.Anchor = CType((System.Windows.Forms.AnchorStyles.Bottom Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.TableLayoutPanel1.ColumnCount = 2
        Me.TableLayoutPanel1.ColumnStyles.Add(New System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50.0!))
        Me.TableLayoutPanel1.ColumnStyles.Add(New System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50.0!))
        Me.TableLayoutPanel1.Controls.Add(Me.cmdCancel, 0, 0)
        Me.TableLayoutPanel1.Controls.Add(Me.cmdOK, 1, 0)
        Me.TableLayoutPanel1.Location = New System.Drawing.Point(188, 372)
        Me.TableLayoutPanel1.Name = "TableLayoutPanel1"
        Me.TableLayoutPanel1.RowCount = 1
        Me.TableLayoutPanel1.RowStyles.Add(New System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50.0!))
        Me.TableLayoutPanel1.Size = New System.Drawing.Size(160, 30)
        Me.TableLayoutPanel1.TabIndex = 0
        '
        'cmdCancel
        '
        Me.cmdCancel.Location = New System.Drawing.Point(3, 3)
        Me.cmdCancel.Name = "cmdCancel"
        Me.cmdCancel.Size = New System.Drawing.Size(74, 23)
        Me.cmdCancel.TabIndex = 0
        Me.cmdCancel.Text = "Cancelar"
        Me.cmdCancel.UseVisualStyleBackColor = True
        '
        'cmdOK
        '
        Me.cmdOK.Location = New System.Drawing.Point(83, 3)
        Me.cmdOK.Name = "cmdOK"
        Me.cmdOK.Size = New System.Drawing.Size(74, 23)
        Me.cmdOK.TabIndex = 1
        Me.cmdOK.Text = "Aceptar"
        Me.cmdOK.UseVisualStyleBackColor = True
        '
        'chkLog
        '
        Me.chkLog.AutoSize = True
        Me.chkLog.Checked = Global.mnuarrayanes.My.MySettings.Default.LogFile
        Me.chkLog.CheckState = System.Windows.Forms.CheckState.Checked
        Me.chkLog.DataBindings.Add(New System.Windows.Forms.Binding("Checked", Global.mnuarrayanes.My.MySettings.Default, "LogFile", True, System.Windows.Forms.DataSourceUpdateMode.OnPropertyChanged))
        Me.chkLog.Location = New System.Drawing.Point(4, 7)
        Me.chkLog.Name = "chkLog"
        Me.chkLog.Size = New System.Drawing.Size(133, 17)
        Me.chkLog.TabIndex = 1
        Me.chkLog.Text = "Habilitar Log (Registro)"
        Me.chkLog.UseVisualStyleBackColor = True
        '
        'lstConnections
        '
        Me.lstConnections.Dock = System.Windows.Forms.DockStyle.Fill
        Me.lstConnections.FormattingEnabled = True
        Me.lstConnections.Location = New System.Drawing.Point(3, 3)
        Me.lstConnections.Name = "lstConnections"
        Me.lstConnections.Size = New System.Drawing.Size(330, 246)
        Me.lstConnections.TabIndex = 2
        '
        'TableLayoutPanel2
        '
        Me.TableLayoutPanel2.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
            Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.TableLayoutPanel2.ColumnCount = 1
        Me.TableLayoutPanel2.ColumnStyles.Add(New System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100.0!))
        Me.TableLayoutPanel2.Controls.Add(Me.lstConnections, 0, 0)
        Me.TableLayoutPanel2.Controls.Add(Me.TableLayoutPanel3, 0, 1)
        Me.TableLayoutPanel2.Controls.Add(Me.TableLayoutPanel4, 0, 2)
        Me.TableLayoutPanel2.Location = New System.Drawing.Point(12, 29)
        Me.TableLayoutPanel2.Name = "TableLayoutPanel2"
        Me.TableLayoutPanel2.RowCount = 3
        Me.TableLayoutPanel2.RowStyles.Add(New System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 75.0!))
        Me.TableLayoutPanel2.RowStyles.Add(New System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 12.5!))
        Me.TableLayoutPanel2.RowStyles.Add(New System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 12.5!))
        Me.TableLayoutPanel2.Size = New System.Drawing.Size(336, 337)
        Me.TableLayoutPanel2.TabIndex = 3
        '
        'TableLayoutPanel3
        '
        Me.TableLayoutPanel3.ColumnCount = 3
        Me.TableLayoutPanel3.ColumnStyles.Add(New System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 33.33333!))
        Me.TableLayoutPanel3.ColumnStyles.Add(New System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 33.33333!))
        Me.TableLayoutPanel3.ColumnStyles.Add(New System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 33.33333!))
        Me.TableLayoutPanel3.Controls.Add(Me.cmdKick, 0, 0)
        Me.TableLayoutPanel3.Controls.Add(Me.cmdReset, 1, 0)
        Me.TableLayoutPanel3.Controls.Add(Me.cmdRefresh, 2, 0)
        Me.TableLayoutPanel3.Dock = System.Windows.Forms.DockStyle.Fill
        Me.TableLayoutPanel3.Location = New System.Drawing.Point(3, 255)
        Me.TableLayoutPanel3.Name = "TableLayoutPanel3"
        Me.TableLayoutPanel3.RowCount = 1
        Me.TableLayoutPanel3.RowStyles.Add(New System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100.0!))
        Me.TableLayoutPanel3.Size = New System.Drawing.Size(330, 36)
        Me.TableLayoutPanel3.TabIndex = 3
        '
        'cmdKick
        '
        Me.cmdKick.Dock = System.Windows.Forms.DockStyle.Fill
        Me.cmdKick.Location = New System.Drawing.Point(3, 3)
        Me.cmdKick.Name = "cmdKick"
        Me.cmdKick.Size = New System.Drawing.Size(104, 30)
        Me.cmdKick.TabIndex = 0
        Me.cmdKick.Text = "Desconectar"
        Me.cmdKick.UseVisualStyleBackColor = True
        '
        'cmdReset
        '
        Me.cmdReset.Dock = System.Windows.Forms.DockStyle.Fill
        Me.cmdReset.Location = New System.Drawing.Point(113, 3)
        Me.cmdReset.Name = "cmdReset"
        Me.cmdReset.Size = New System.Drawing.Size(104, 30)
        Me.cmdReset.TabIndex = 1
        Me.cmdReset.Text = "Resetear"
        Me.cmdReset.UseVisualStyleBackColor = True
        '
        'cmdRefresh
        '
        Me.cmdRefresh.Dock = System.Windows.Forms.DockStyle.Fill
        Me.cmdRefresh.Location = New System.Drawing.Point(223, 3)
        Me.cmdRefresh.Name = "cmdRefresh"
        Me.cmdRefresh.Size = New System.Drawing.Size(104, 30)
        Me.cmdRefresh.TabIndex = 2
        Me.cmdRefresh.Text = "Actualizar"
        Me.cmdRefresh.UseVisualStyleBackColor = True
        '
        'TableLayoutPanel4
        '
        Me.TableLayoutPanel4.ColumnCount = 2
        Me.TableLayoutPanel4.ColumnStyles.Add(New System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50.0!))
        Me.TableLayoutPanel4.ColumnStyles.Add(New System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50.0!))
        Me.TableLayoutPanel4.Controls.Add(Me.lblIP, 0, 0)
        Me.TableLayoutPanel4.Controls.Add(Me.lblPort, 1, 0)
        Me.TableLayoutPanel4.Dock = System.Windows.Forms.DockStyle.Fill
        Me.TableLayoutPanel4.Location = New System.Drawing.Point(3, 297)
        Me.TableLayoutPanel4.Name = "TableLayoutPanel4"
        Me.TableLayoutPanel4.RowCount = 1
        Me.TableLayoutPanel4.RowStyles.Add(New System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50.0!))
        Me.TableLayoutPanel4.Size = New System.Drawing.Size(330, 37)
        Me.TableLayoutPanel4.TabIndex = 4
        '
        'lblIP
        '
        Me.lblIP.Anchor = System.Windows.Forms.AnchorStyles.Left
        Me.lblIP.AutoSize = True
        Me.lblIP.Location = New System.Drawing.Point(3, 12)
        Me.lblIP.Name = "lblIP"
        Me.lblIP.Size = New System.Drawing.Size(0, 13)
        Me.lblIP.TabIndex = 0
        '
        'lblPort
        '
        Me.lblPort.Anchor = System.Windows.Forms.AnchorStyles.Left
        Me.lblPort.AutoSize = True
        Me.lblPort.Location = New System.Drawing.Point(168, 12)
        Me.lblPort.Name = "lblPort"
        Me.lblPort.Size = New System.Drawing.Size(0, 13)
        Me.lblPort.TabIndex = 1
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Location = New System.Drawing.Point(15, 13)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(123, 13)
        Me.Label1.TabIndex = 4
        Me.Label1.Text = "Dispositivos Conectados"
        '
        'Panel1
        '
        Me.Panel1.Anchor = CType(((System.Windows.Forms.AnchorStyles.Bottom Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.Panel1.Controls.Add(Me.chkLog)
        Me.Panel1.Location = New System.Drawing.Point(15, 372)
        Me.Panel1.Name = "Panel1"
        Me.Panel1.Size = New System.Drawing.Size(146, 30)
        Me.Panel1.TabIndex = 5
        '
        'frmSettings
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(360, 414)
        Me.Controls.Add(Me.Panel1)
        Me.Controls.Add(Me.Label1)
        Me.Controls.Add(Me.TableLayoutPanel2)
        Me.Controls.Add(Me.TableLayoutPanel1)
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.SizableToolWindow
        Me.Icon = CType(resources.GetObject("$this.Icon"), System.Drawing.Icon)
        Me.MinimumSize = New System.Drawing.Size(376, 306)
        Me.Name = "frmSettings"
        Me.Text = "Opciones"
        Me.TableLayoutPanel1.ResumeLayout(False)
        Me.TableLayoutPanel2.ResumeLayout(False)
        Me.TableLayoutPanel3.ResumeLayout(False)
        Me.TableLayoutPanel4.ResumeLayout(False)
        Me.TableLayoutPanel4.PerformLayout()
        Me.Panel1.ResumeLayout(False)
        Me.Panel1.PerformLayout()
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub

    Friend WithEvents TableLayoutPanel1 As TableLayoutPanel
    Friend WithEvents cmdCancel As Button
    Friend WithEvents cmdOK As Button
    Friend WithEvents chkLog As CheckBox
    Friend WithEvents lstConnections As ListBox
    Friend WithEvents TableLayoutPanel2 As TableLayoutPanel
    Friend WithEvents TableLayoutPanel3 As TableLayoutPanel
    Friend WithEvents Label1 As Label
    Friend WithEvents cmdKick As Button
    Friend WithEvents cmdReset As Button
    Friend WithEvents cmdRefresh As Button
    Friend WithEvents Panel1 As Panel
    Friend WithEvents TableLayoutPanel4 As TableLayoutPanel
    Friend WithEvents lblIP As Label
    Friend WithEvents lblPort As Label
End Class
