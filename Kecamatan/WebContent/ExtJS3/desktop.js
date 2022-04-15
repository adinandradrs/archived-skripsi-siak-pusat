MyDesktop = new Ext.app.App({
	init :function(){
		Ext.QuickTips.init();
	},

	getModules : function(){
		return [
			new MyDesktop.BiodataApplication(),
            new MyDesktop.PendudukApplication(),
            new MyDesktop.DokumenApplication(),
            new MyDesktop.PermohonanApplication()
		];
	},

    getStartConfig : function(){
        return {
            title: username,
            iconCls: 'user',
            toolItems: [
            {
            	xtype:'button',
                text:'Logout',
                iconCls:'logout',
                handler:function(){
                	window.location = "LogoutController";
                },
                scope:this
            }]
        };
    }
});


MyDesktop.BiodataApplication = Ext.extend(Ext.app.Module, {
    id:'bio-win',
    init : function(){
        this.launcher = {
            text: 'Biodata',
            iconCls:'biodata',
            handler : this.createWindow,
            scope: this
        }
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('bio-win');
        if(!win){
            win = desktop.createWindow({
                id: 'bio-win',
                title:'Biodata',
                width:900,
                height:580,
                iconCls: 'biodata',
                shim:false,
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                html:'<iframe src="Pages/Biodata.jsp" style="width:100%;height:100%;"></iframe>'
            });
        }
        win.show();
    }
});


MyDesktop.PendudukApplication = Ext.extend(Ext.app.Module, {
    id:'pen-win',
    init : function(){
        this.launcher = {
            text: 'Penduduk',
            iconCls:'civil',
            handler : this.createWindow,
            scope: this
        }
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('pen-win');
        if(!win){
            win = desktop.createWindow({
                id: 'pen-win',
                title: 'Penduduk',
                width:1000,
                height:500,
                iconCls: 'civil',
                layout: 'fit',
                html:'<iframe src="Pages/Penduduk.jsp" style="width:100%;height:100%;"></iframe>'
            });
        }
        win.show();
    }
});

MyDesktop.PermohonanApplication = Ext.extend(Ext.app.Module, {
    id:'per-win',
    init : function(){
        this.launcher = {
            text: 'Permohonan',
            iconCls:'permohonan',
            handler : this.createWindow,
            scope: this
        }
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('per-win');
        if(!win){
            win = desktop.createWindow({
                id: 'per-win',
                title: 'Permohonan',
                width:700,
                height:400,
                iconCls: 'permohonan',
                layout: 'fit',
                html:'<iframe src="Pages/Permohonan.jsp" style="width:100%;height:100%;"></iframe>'
            });
        }
        win.show();
    }
});

MyDesktop.DokumenApplication = Ext.extend(Ext.app.Module, {
    id:'doc-win',
    init : function(){
        this.launcher = {
            text: 'Dokumen',
            iconCls:'document',
            handler: function() {
				return false;
			},
			menu : {
                items:[
                   {
                	   text: 'KTP',
                	   scope: this,
                	   handler:function(){
                		   var desktop = this.app.getDesktop();
                		   var win = desktop.getWindow('doc-win');
                		   if(!win){
                			   win = desktop.createWindow({
                				   id: 'doc-win',
                				   title: 'Dokumen KTP',
                				   width:850,
                				   height:500,
                				   iconCls: 'document',
                				   layout: 'fit',
                				   html:'<iframe src="Pages/Dokumen.jsp?jenisDokumen=ktp" style="width:100%;height:100%;"></iframe>'
                			   });
                		    }
                		    win.show();
                	   }
                   },
                   {
                	   text: 'Kartu Keluarga',
                	   scope: this,
                	   handler:function(){
                		   var desktop = this.app.getDesktop();
                		   var win = desktop.getWindow('doc-win');
                		   if(!win){
                			   win = desktop.createWindow({
                				   id: 'doc-win',
                				   title: 'Dokumen KK',
                				   width:850,
                				   height:500,
                				   iconCls: 'document',
                				   layout: 'fit',
                				   html:'<iframe src="Pages/Dokumen.jsp?jenisDokumen=kartuKeluarga" style="width:100%;height:100%;"></iframe>'
                			   });
                		    }
                		    win.show();
                	   }
                   }
                ]
			},
            scope: this
        }
    }
});
