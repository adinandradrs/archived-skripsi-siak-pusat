/*

This file is part of Ext JS 4

Copyright (c) 2011 Sencha Inc

Contact:  http://www.sencha.com/contact

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.sencha.com/contact.

*/
/*!
 * Ext JS Library 3.3.1
 * Copyright(c) 2006-2010 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

// Sample desktop configuration
MyDesktop = new Ext.app.App({
	init :function(){
		Ext.QuickTips.init();
	},

	getModules : function(){
		return [
			new MyDesktop.DataPokokApplication(),
            new MyDesktop.PenggunaApplication(),
            new MyDesktop.PendudukApplication(),
            new MyDesktop.DokumenApplication(),
            new MyDesktop.PermohonanApplication(),
            new MyDesktop.PortalApplication(),
            new MyDesktop.LogApplication(),
            new MyDesktop.DashboardApplication()
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


MyDesktop.DataPokokApplication = Ext.extend(Ext.app.Module, {
    id:'dapo-win',
    init : function(){
        this.launcher = {
            text: 'Data Pokok',
            iconCls:'pokok',
            handler : this.createWindow,
            scope: this
        }
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('dapo-win');
        if(!win){
            win = desktop.createWindow({
                id: 'dapo-win',
                title:'Data Pokok',
                width:1000,
                height:500,
                iconCls: 'pokok',
                shim:false,
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                html:'<iframe src="Pages/DataPokok.jsp" style="width:100%;height:100%;"></iframe>'
            });
        }
        win.show();
    }
});

MyDesktop.PenggunaApplication = Ext.extend(Ext.app.Module, {
    id:'peng-win',
    init : function(){
        this.launcher = {
            text: 'Pengguna',
            iconCls:'myuser',
            handler : this.createWindow,
            scope: this
        }
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('peng-win');
        if(!win){
            win = desktop.createWindow({
                id: 'peng-win',
                title:'Data Pengguna',
                width:1000,
                height:500,
                iconCls: 'myuser',
                shim:false,
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                html:'<iframe src="Pages/Pengguna.jsp" style="width:100%;height:100%;"></iframe>'
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

MyDesktop.DashboardApplication = Ext.extend(Ext.app.Module, {
    id:'das-win',
    init : function(){
        this.launcher = {
            text: 'Dashboard',
            iconCls:'dashboard',
            handler : this.createWindow,
            scope: this
        }
    },
    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('per-win');
        if(!win){
            win = desktop.createWindow({
                id: 'das-win',
                title: 'Dashboard',
                width:1000,
                height:600,
                iconCls: 'dashboard',
                layout: 'fit',
                html:'<iframe src="Pages/Dashboard.jsp" style="width:100%;height:100%;"></iframe>'
            });
        }
        win.show();
    }
});

MyDesktop.PortalApplication = Ext.extend(Ext.app.Module, {
    id:'por-win',
    init : function(){
        this.launcher = {
            text: 'Portal',
            iconCls:'wizard',
            //handler : this.createWindow,
            scope: this
        }
    }
});

MyDesktop.LogApplication = Ext.extend(Ext.app.Module, {
    id:'log-win',
    init : function(){
        this.launcher = {
            text: 'Log SIAK',
            iconCls:'log',
            handler : this.createWindow,
            scope: this
        }
    },
    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('log-win');
        if(!win){
            win = desktop.createWindow({
                id: 'log-win',
                title:'Log',
                width:800,
                height:500,
                iconCls: 'log',
                shim:false,
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                html:'<iframe src="Pages/Log.jsp" style="width:100%;height:100%;"></iframe>'
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
                	   text: 'Akta Nikah',
                	   scope: this,
                	   handler: function(){
                		   var desktop = this.app.getDesktop();
                		   var win = desktop.getWindow('doc-win');
                		   if(!win){
                			   win = desktop.createWindow({
                				   id: 'doc-win',
                				   title: 'Dokumen Akta Nikah',
                				   width:800,
                				   height:500,
                				   iconCls: 'document',
                				   layout: 'fit',
                				   html:'<iframe src="Pages/Dokumen.jsp?jenisDokumen=aktaNikah" style="width:100%;height:100%;"></iframe>'
                			   });
                		    }
                		    win.show();
                	   }
                   },
                   {
                	   text: 'Akta Kelahiran',
                	   scope: this,
                	   handler:function(){
                		   var desktop = this.app.getDesktop();
                		   var win = desktop.getWindow('doc-win');
                		   if(!win){
                			   win = desktop.createWindow({
                				   id: 'doc-win',
                				   title: 'Dokumen Akta Lahir',
                				   width:800,
                				   height:500,
                				   iconCls: 'document',
                				   layout: 'fit',
                				   html:'<iframe src="Pages/Dokumen.jsp?jenisDokumen=aktaKelahiran" style="width:100%;height:100%;"></iframe>'
                			   });
                		    }
                		    win.show();
                	   }
                   },
                   {
                	   text: 'Akta Kematian',
                	   scope : this,
                	   handler:function(){
                		   var desktop = this.app.getDesktop();
                		   var win = desktop.getWindow('doc-win');
                		   if(!win){
                			   win = desktop.createWindow({
                				   id: 'doc-win',
                				   title: 'Dokumen Akta Kematian',
                				   width:800,
                				   height:500,
                				   iconCls: 'document',
                				   layout: 'fit',
                				   html:'<iframe src="Pages/Dokumen.jsp?jenisDokumen=aktaKematian" style="width:100%;height:100%;"></iframe>'
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
