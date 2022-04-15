var aktaKematianStore;

var aktaKematianGrid;

var noAktaKematian;
var nik;
var nikAyah;
var nikIbu;
var tanggalMeninggal;
var tempatMeninggal;
var keterangan;
var noKK;

Ext.onReady(function(){
	
	Ext.define('kartuKeluarga',{
		extend : 'Ext.data.Model',
		fields :[
		{
			name : 'noKk',
			type : 'string'
		}
        ]
	});
	
	var kkStore = Ext.create('Ext.data.Store',{
		model : 'kartuKeluarga',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../KartuKeluargaController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
		autoLoad : true
	});
	
	Ext.define('Biodata',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'nik',
			type : 'string'
		}
        ]
	});
	
	Ext.define('Biodata1',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'nik',
			type : 'string'
		}
        ]
	});
	
	Ext.define('Biodata2',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'nik',
			type : 'string'
		}
        ]
	});
	
	var bioStore = Ext.create('Ext.data.Store', {
		model : 'Biodata1',
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
	
	Ext.define('aktaKematian',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'noAktaKematian',
			type : 'string'
		},
		{
			name : 'nama',
			type : 'string'
		},
		{
			name : 'tanggalMeninggal',
			type : 'string'
		},
		{
			name : 'tempatMeninggal',
			type : 'string'
		},
		{
			name : 'keterangan',
			type : 'string'
		},
		{
			name : 'namaAyah',
			type : 'string'
		},
		{
			name : 'namaIbu',
			type : 'string'
		},
		{
			name : 'jenisBuat',
			type : 'string'
		},
		{
			name : 'noKk',
			type : 'string'
		}
        ]
	});
	
	aktaKematianStore = Ext.create('Ext.data.Store', {
		model : 'aktaKematian',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../AktaKematianController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	var newRow = false;
	var aktaKematianGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	aktaKematianGrid = Ext.create('Ext.grid.Panel',{
		store: aktaKematianStore,
	    anchor:'100% 100%',
	    plugins:[aktaKematianGridEditing],
	    columns: [
	              {header:'No. Akta Kematian', dataIndex: 'noAktaKematian',editor:{
	            	  id:'noAktaKematianTextField',
	            	  allowBlank:false
	              }},
	              {header:'Nama', dataIndex: 'nama', editor:{
	            	  id:'nikComboBox',
	            	  xtype:'combobox',
	            	  store:bioStore,
	            	  displayField:'nik',
	            	  valueField:'nik',
	            	  allowBlank:false,
	            	  queryMode: 'local'
	              }},
	              {
	            	  header:'Meninggal',
	            	  columns:[
	            	           {
	            	        	   header:'Tanggal Meninggal', dataIndex: 'tanggalMeninggal', editor:{
	            	        		   id: 'tanggalMeninggalDateField',
	            	        		   xtype:'datefield',
	            	        		   format : 'Y-m-d',
	            	        		   allowBlank:false
	            	        	   }
	            	           },
	            	           {
	            	        	   header:'Tempat Meninggal', dataIndex: 'tempatMeninggal', editor:{
	            	        		   id: 'tempatMeninggalTextField',
	            	        		   allowBlank:false
	            	        	   }
	            	           },
	            	           {
	            	        	   header:'Keterangan', dataIndex: 'keterangan', editor:{
	            	        		   id: 'keteranganTextField',
	            	        		   allowBlank:false
	            	        	   }
	            	           }
	            	  ],
	            	  forceFit : true
	              },
	              {
	            	  header: 'Orang Tua',
	            	  columns:[
	            	           {header:'Nama Ayah', dataIndex:'namaAyah'},
	            	           {header:'Nama Ibu', dataIndex:'namaIbu'},
	            	           {header:'Kartu Keluarga', dataIndex:'noKk', editor:{
	            	        	   id:'noKKComboBox',
	            	        	   xtype:'combobox',
	            	        	   store:kkStore,
	            	        	   displayField:'noKk',
	            	        	   valueField:'noKk',
	            	        	   allowBlank:false,
	            		           queryMode: 'local'
	            	           }}
            	      ],
            	      forceFit:true
            	  },
            	  {header:'Jenis Buat', dataIndex:'jenisBuat'}
        ],
        forceFit : true,
        bbar :Ext.create('Ext.PagingToolbar', {
            store: aktaKematianStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display",
            items:[
				{
					  text : 'Tambah',
					  iconCls : 'add',
					  handler : function(){
						  aktaKematianGridEditing.cancelEdit();
				          var r = Ext.create('aktaKematian', {
				        	  noAktaKematian : 'No Akta Kematian',
				        	  nama : 'Nama Jenazah',
				        	  tanggalMeninggal:'01-01-2000',
				        	  namaAyah : 'Nama Ayah',
				        	  namaIbu : 'Nama Ibu'
				          });
				          aktaKematianStore.insert(0, r);
				          aktaKematianGridEditing.startEdit(0, 0);
				          newRow = true;
					  }
				},
				{
					  text : 'Hapus',
					  iconCls : 'delete',
					  handler: function(){
						  postDeleteAktaKematian();
					  }
				},
				{
					  text : 'Cetak',
					  iconCls : 'cetakdk2',
					  handler: function(){
						  postCetakAktaKematian();
					  }
				}
            ]
        })
	});
	
	aktaKematianGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        aktaKematianStore.removeAt(0);
	    newRow = false;
	});
	
	aktaKematianGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    if(newRow == true){
	    	noAktaKematian = Ext.getCmp('noAktaKematianTextField').getValue();
	    	nik = Ext.getCmp('nikComboBox').getValue();
	    	noKK = Ext.getCmp('noKKComboBox').getValue();
	    	tempatMeninggal = Ext.getCmp('tempatMeninggalTextField').getValue();
	    	keterangan = Ext.getCmp('keteranganTextField').getValue();
	    	tanggalMeninggal = Ext.getCmp('tanggalMeninggalDateField').getValue();
	    	postAddAktaKematian();
	    }
	    else{
	    	
	    }
	},this);
	
	aktaKematianGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			noAktaKematian= selectedRecord[0].data.noAktaKematian;
		}
		catch(err){
			
		}
	});
	
	Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [{
	        region: 'center',
	        layout:'anchor',
	        id:'aktaKematianPanel',
	        items:[
	               	aktaKematianGrid
	        ]
	    }]
	});
	
});

function postAddAktaKematian(){
	Ext.Ajax.request({
        url: '../AktaKematianController',
        params: { 
        	'command' : 'add',
        	'noAktaKematian':noAktaKematian,
        	'nik':nik,
        	'tanggalMeninggal':tanggalMeninggal,
        	'tempatMeninggal' : tempatMeninggal,
        	'keterangan' : keterangan,
        	'noKK' : noKK
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
        		aktaKematianStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteAktaKematian(){
	Ext.Ajax.request({
        url: '../AktaKematianController',
        params: { 
        	'command' : 'delete',
        	'noAktaKematian':noAktaKematian
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
        		aktaKematianStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postCetakAktaKematian(){
	Ext.Ajax.request({
        url: '../AktaKematianController',
        params: { 
        	'command' : 'print',
        	'noAktaKematian':noAktaKematian
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