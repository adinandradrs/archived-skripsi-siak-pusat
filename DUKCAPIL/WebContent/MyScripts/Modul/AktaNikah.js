var aktaNikahStore;
var detailNikahStore;
var bioStore;

var aktaNikahGrid;
var detailNikahGrid;
var bioGrid;

var newRow = false;
var aktaNikahGridEditing;

var noAktaNikah;
var nikSuami;
var nikIstri;
var aktaNikahDetailID;

/*************************/
var encode;
var local;
var filters;
/*************************/

Ext.onReady(function(){

	var aktaNikahMenu = Ext.create('Ext.panel.Panel', {
	    title: 'Menu Utama',
	    anchor:'100%',
	    region:'north',
	    tbar: [{
	    	xtype: 'buttongroup',
	        columns: 4,
	        title: 'Pilihan',
	        collapsible:true,
	        items: [
				{
	        		text: 'Tambah',
	        		scale: 'large',
	        		iconCls: 'add-large',
	                iconAlign: 'top',
	                handler:function(){
	                	aktaNikahGridEditing.cancelEdit();
		                var r = Ext.create('aktaNikah', {
		                	noAktaNikah : "No. Akta Nikah",
		                	jenisBuat : "-",
		                	tanggalBuat : "-"
		                 });
		                 aktaNikahStore.insert(0, r);
		                 aktaNikahGridEditing.startEdit(0, 0);
		                 newRow = true;
	                }
	        	},
	        	{
	        		text: 'Hapus',
					scale: 'large',
					iconCls: 'delete-large',
				    iconAlign: 'top',
				    handler:function(){
				    	postDeleteAktaNikah();
				    }
	        	},
	        	{
					text: 'Data',
					scale: 'large',
					iconCls: 'tabeldk',
				    iconAlign: 'top',
				    handler:function(){
				    	if(noAktaNikah != null){
				    		callAktaNikahDetailGrid();
				    	}
				    }
				},                
				{
                	text: 'Cetak',
                	scale: 'large',
                	iconCls: 'cetakdk',
                    iconAlign: 'top',
                    handler:function(){
                    	postCetakAktaNikah();
                    }
	        	}
	        ]
	    }]
	});
	
	Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [
	            aktaNikahMenu,
	        {
		        region: 'center',
		        layout:'anchor',
		        id:'aktaNikahPanel',
		        items:[
		               callAktaNikahGrid()
	            ]
		    }
        ]
	});
	
});

function callAktaNikahGrid(){
	newRow = false;
	aktaNikahGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	Ext.define('aktaNikah',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'noAktaNikah',
			type : 'string',
		},
		{
			name : 'jenisBuat',
			type : 'string',
		},
		{
			name : 'tanggalBuat',
			type : 'string',
		}
        ]
	});
	
	aktaNikahStore = Ext.create('Ext.data.Store', {
		model : 'aktaNikah',
		proxy : {
			type: 'ajax',
	        url : '../AktaNikahController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	aktaNikahGrid = Ext.create('Ext.grid.Panel', {
	    title: 'Data Akta Nikah',
	    store: aktaNikahStore,
	    anchor:'100% 100%',
	    columns: [
            { header: 'No. Akta',  dataIndex: 'noAktaNikah', editor:{
            	id:'noAktaNikahTextField',
            	allowBlank:false
            }},
            { header: 'Jenis Buat',  dataIndex: 'jenisBuat', editor:{
            	id:'jenisBuatComboBox',
            	xtype:'combobox',
            	store:jenisCetakStore,
            	displayField:'name',
            	valueField:'id'
            }},
            { header: 'Tanggal Cetak', dataIndex : 'tanggalBuat'}
	    ],
	    plugins: [aktaNikahGridEditing],
	    forceFit : true,
	    bbar :Ext.create('Ext.PagingToolbar', {
            store: aktaNikahStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display"
	    })
	});
	
	aktaNikahGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	    	aktaNikahStore.removeAt(0);
	    newRow = false;
	});
	
	aktaNikahGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    if(newRow == true){
	    	noAktaNikah = o.record.get('noAktaNikah');
    		postAddAktaNikah();
		    newRow = false;
	    }
	    else{
	    	
	    }
	},this);
	
	aktaNikahGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			noAktaNikah = selectedRecord[0].data.noAktaNikah;
		}
		catch(err){
			
		}
	});
	
	return aktaNikahGrid;
}

function callAktaNikahDetailGrid(){
	Ext.define('Biodata',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'nik',
			type : 'string'
		},
		{
			name : 'nama',
			type : 'string'
		},
		{
			name : 'tanggalLahir',
			type : 'string'
		},
		{
			name : 'agama',
			type : 'string'
		},
		{
			name : 'pekerjaan',
			type : 'string'
		},
		{
			name : 'pendidikan',
			type : 'string'
		},
		{
			name : 'jenisKelamin',
			type : 'string'
		},
		{
			name : 'aktaKelahiran',
			type : 'string'
		},
		{
			name : 'kartuKeluarga',
			type : 'string'
		},
		{
			name : 'aktif',
			type : 'bool'
		},
		{
			name : 'golonganDarah',
			type : 'string'
		},
		{
			name : 'tempatLahir',
			type : 'string'
		},
		{
			name : 'foto',
			type : 'string'
		},
		{
			name : 'kecamatan',
			type : 'string'
		},
		{
			name : 'kelurahan',
			type : 'string'
		}
        ]
	});
	
	bioStore = Ext.create('Ext.data.Store', {
		model : 'Biodata',
		pageSize : 12,
	    groupField: 'kecamatan',
		proxy : {
			type: 'ajax',
	        url : '../PendudukController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	newRow = false;
	detailNikahGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	Ext.define('aktaNikahDetail',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'aktaNikahDetailId',
			type : 'string'
		},
		{
			name : 'nikSuami',
			type : 'string'
		},
		{
			name : 'nikIstri',
			type : 'string'
		},
		{
			name : 'namaSuami',
			type : 'string'
		},
		{
			name : 'namaIstri',
			type : 'string'
		}
		]
	});
	
	detailNikahStore = Ext.create('Ext.data.Store', {
		model : 'aktaNikahDetail',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../AktaNikahDetailController?command=load&noAktaNikah='+noAktaNikah,
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	detailNikahGrid = Ext.create('Ext.grid.Panel',{
		store:detailNikahStore,
		anchor:'100% 100%',
	    columns: [
            {header:'NIK Suami',dataIndex:'namaSuami', editor:{
            	xtype:'combobox',
            	id:'nikSuamiComboBox',
            	store:bioStore,
            	displayField:'nik',
            	valuedField : 'nik',
	            queryMode: 'local'
            }},
            {header:'NIK Istri',dataIndex:'namaIstri', editor:{
            	xtype:'combobox',
            	id:'nikIstriComboBox',
            	store:bioStore,
            	displayField:'nik',
            	valuedField : 'nik',
	            queryMode: 'local'
            }}
	    ],
	    plugins: [detailNikahGridEditing],
	    forceFit : true,
	    bbar :Ext.create('Ext.PagingToolbar', {
            store: detailNikahStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display",
            items:[
                   {
                	   text:'Tambah',
                	   iconCls:'add',
                	   handler:function(){
                		   var nikLength = detailNikahStore.getAt(0).get("nikSuami").length;
                		   if(nikLength == 0){
	                		   detailNikahGridEditing.cancelEdit();
	                		   var r = Ext.create('aktaNikah', {
	                			   nikSuami : 'NIK SUAMI',
	                			   nikIstri : 'NIK ISTRI'
	                		   });
	                		   detailNikahStore.insert(0, r);
	                		   detailNikahGridEditing.startEdit(0, 0);
	                		   newRow = true;
                		   }
                	   }
                   },
                   {
                	   text:'Hapus',
                	   iconCls:'delete',
                	   handler:function(){
                		   postDeleteAktaNikahDetail();
                	   }
                   }
            ]
	    })
	});
	
	detailNikahGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	    	detailNikahStore.removeAt(0);
	    newRow = false;
	});
	
	detailNikahGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    if(newRow == true){
	    	nikSuami = Ext.getCmp('nikSuamiComboBox').getValue();
	    	nikIstri = Ext.getCmp('nikIstriComboBox').getValue();
	    	postAddAktaNikahDetail();
		    newRow = false;
	    }
	    else{
	    	
	    }
	},this);
	
	detailNikahGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			aktaNikahDetailID = selectedRecord[0].data.aktaNikahDetailId;
		}
		catch(err){
			
		}
	});
	
	var cascadePanel = Ext.create('Ext.panel.Panel', {
	    title:'Detail Kartu Keluarga',
		anchor:'100% 100%',
		layout:'anchor',
		items:[detailNikahGrid]
	});
	
	Ext.create('Ext.window.Window', {
	    height: 400,
	    width: 750,
	    layout: 'anchor',
	    items: [cascadePanel]
	}).show();
}

function postAddAktaNikah(){
	Ext.Ajax.request({
        url: '../AktaNikahController',
        params: { 
            "command" : "add",
            "noAktaNikah" : noAktaNikah
        },
        success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		Ext.MessageBox.show({
  				  modal : false,
  		          title: 'Peringatan',
  		          msg: obj.explain,
  		          buttons: Ext.MessageBox.OK,
  		          icon: Ext.MessageBox.ERROR
  		      	});
        	}
        	else{
        		aktaNikahStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteAktaNikah(){
	Ext.Ajax.request({
        url: '../AktaNikahController',
        params: { 
            "command" : "delete",
            "noAktaNikah" : noAktaNikah
        },
        success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		Ext.MessageBox.show({
  				  modal : false,
  		          title: 'Peringatan',
  		          msg: obj.explain,
  		          buttons: Ext.MessageBox.OK,
  		          icon: Ext.MessageBox.ERROR
  		      	});
        	}
        	else{
        		aktaNikahStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddAktaNikahDetail(){
	Ext.Ajax.request({
        url: '../AktaNikahDetailController',
        params: { 
            "command" : "add",
            "noAktaNikah" : noAktaNikah,
            "nikSuami" : nikSuami,
            "nikIstri" : nikIstri
        },
        success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		Ext.MessageBox.show({
  				  modal : false,
  		          title: 'Peringatan',
  		          msg: obj.explain,
  		          buttons: Ext.MessageBox.OK,
  		          icon: Ext.MessageBox.ERROR
  		      	});
        	}
        	else{
        		detailNikahStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteAktaNikahDetail(){
	Ext.Ajax.request({
        url: '../AktaNikahDetailController',
        params: { 
            "command" : "delete",
            "aktaNikahDetailID" : aktaNikahDetailID
        },
        success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		Ext.MessageBox.show({
  				  modal : false,
  		          title: 'Peringatan',
  		          msg: obj.explain,
  		          buttons: Ext.MessageBox.OK,
  		          icon: Ext.MessageBox.ERROR
  		      	});
        	}
        	else{
        		detailNikahStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postCetakAktaNikah(){
	Ext.Ajax.request({
        url: '../AktaNikahController',
        params: { 
            "command" : "print",
            "noAktaNikah" : noAktaNikah
        },
        success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		Ext.MessageBox.show({
  				  modal : false,
  		          title: 'Peringatan',
  		          msg: obj.explain,
  		          buttons: Ext.MessageBox.OK
  		      	});
        	}
        	else{
        		Ext.MessageBox.show({
				  modal : false,
		          title: 'Peringatan',
		          msg: obj.explain,
		          buttons: Ext.MessageBox.OK
		      	});
        	}
        },
        failure: function(response, opts){

        }
    });
}