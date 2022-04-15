var aktaKelahiranStore;
var aktaKelahiranDetailStore;

var aktaKelahiranGrid;
var aktaKelahiranDetailGrid;

var noAktaKelahiran;
var nama;
var tanggalLahir;
var tempatLahir;
var noAktaNikah;
var jenisKelamin;
var noKK;
var jenisKelahiran;
var bantuanKelahiran;

var nikAyah;
var nikIbu;

Ext.onReady(function(){
	
	/*****STORE CORE DATA*******/
	var kelaminStore = Ext.create('Ext.data.Store', {
	    fields: ['text'],
	    data : [
	        {"text":"Laki-Laki"},
	        {"text":"Perempuan"}
	    ]
	});
	
	var jenisKelahiranStore = Ext.create('Ext.data.Store', {
	    fields: ['text'],
	    data : [
	        {"text":"Tunggal"},
	        {"text":"Kembar 2"},
	        {"text":"Kembar 3"},
	        {"text":"Kembar 4"},
	        {"text":"-"}
	    ]
	});
	
	var bantuanKelahiranStore = Ext.create('Ext.data.Store', {
	    fields: ['text'],
	    data : [
	        {"text":"Bidan"},
	        {"text":"Dokter"},
	        {"text":"Dukun"}
	    ]
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
	
	var bioStore1 = Ext.create('Ext.data.Store', {
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
	
	var bioStore2 = Ext.create('Ext.data.Store', {
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
	
	Ext.define('aktaNikah',{
		extend : 'Ext.data.Model',
		fields :[
		{
			name : 'noAktaNikah',
			type : 'string'
		}
        ]
	});
	
	var aktaNikahStore = Ext.create('Ext.data.Store',{
		model : 'aktaNikah',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../AktaNikahController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
		autoLoad : true
	});
	/***************************/
	
	Ext.define('aktaKelahiran',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'noAktaKelahiran',
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
			name : 'tempatLahir',
			type : 'string'
		},
		{
			name : 'nikAyah',
			type : 'string'
		},
		{
			name : 'nikIbu',
			type : 'string'
		},
		{
			name : 'jenisKelamin',
			type : 'string'
		},
		{
			name : 'tanggalBuat',
			type : 'string'
		},
		{
			name : 'noKK',
			type : 'string'
		},
		{
			name : 'jenisKelahiran',
			type : 'string'
		},
		{
			name : 'bantuanKelahiran',
			type : 'string'
		},
		{
			name : 'jenisBuat',
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
			name : 'golDarahAyah',
			type : 'string'
		},
		{
			name : 'golDarahIbu',
			type : 'string'
		}
        ]
	});
	
	aktaKelahiranStore = Ext.create('Ext.data.Store',{
		model : 'aktaKelahiran',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../AktaKelahiranController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	var newRow = false;
	var aktaKelahiranGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	aktaKelahiranGrid = Ext.create('Ext.grid.Panel',{
		store: aktaKelahiranStore,
	    anchor:'100% 100%',
	    plugins:[aktaKelahiranGridEditing],
	    columns: [
            {header:'No. Akta Lahir', dataIndex:'noAktaKelahiran',editor:{
            	id:'noAktaKelahiranTextField',
            	allowBlank : false
            }},
            {header:'Nama', dataIndex:'nama',editor:{
            	id:'namaTextField',
            	allowBlank : false
            }},
            {
                text: 'Bayi',
                columns: [
                	{header:'Jenis Kelamin', dataIndex:'jenisKelamin',editor:{
                    	id:'jenisKelaminComboBox',
                    	xtype:'combobox',
                    	store:kelaminStore,
                    	displayfield: 'text',
                    	valueField:'text',
                    	allowBlank : false
                    }},
                    {header:'Tanggal Lahir', dataIndex:'tanggalLahir',editor:{
                    	id:'tanggalLahirDateField',
                    	xtype:'datefield',
                    	format : 'Y-m-d',
                    	allowBlank : false
                    }},
                    {header:'Tempat Lahir', dataIndex:'tempatLahir',editor:{
                    	id:'tempatLahirTextField',
                    	allowBlank : false
                    }}
                ],
        	    forceFit : true
            },
            {
                text: 'Orang Tua',
                columns: [
                	{header:'NIK Ayah', dataIndex:'namaAyah',editor:{
                    	id:'aktaNikahComboBox',
                    	xtype:'combobox',
                    	store:aktaNikahStore,
                    	valueField:'noAktaNikah',
                    	displayField:'noAktaNikah',
                    	allowBlank : false,
        	            queryMode: 'local'
                    }},
                	{header:'NIK Ibu', dataIndex:'namaIbu'}
                ],
        	    forceFit : true
            },
            {
                text: 'Kelengkapan Lainnya',
                columns: [
                	{header:'Tanggal Buat', dataIndex:'tanggalBuat'},
		            {header:'No KK', dataIndex:'noKK',editor:{
                    	id:'kkComboBox',
                    	xtype:'combobox',
                    	store:kkStore,
                    	valueField:'noKk',
                    	displayField:'noKk',
                    	allowBlank : false,
        	            queryMode: 'local'
                    }},
		            {header:'Jenis Kelahiran', dataIndex:'jenisKelahiran',editor:{
                    	id:'jenisKelahiranComboBox',
                    	xtype:'combobox',
                    	store:jenisKelahiranStore,
                    	valueField:'text',
                    	displayField:'text',
                    	allowBlank : false
                    }},
		            {header:'Bantuan Kelahiran', dataIndex:'bantuanKelahiran',editor:{
                    	id:'bantuanKelahiranComboBox',
                    	xtype:'combobox',
                    	store:bantuanKelahiranStore,
                    	valueField:'text',
                    	displayField:'text',
                    	allowBlank : false
                    }},
		            {header:'Jenis Buat', dataIndex:'jenisBuat'}
                ],
        	    forceFit : true
            }
	    ],
	    forceFit : true,
	    bbar :Ext.create('Ext.PagingToolbar', {
            store: aktaKelahiranStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display",
            items:[
				{
					  text : 'Tambah',
					  iconCls : 'add',
					  handler : function(){
						  aktaKelahiranGridEditing.cancelEdit();
				          var r = Ext.create('aktaKelahiran', {
				        	  noAktaKelahiran:'No Akta Kelahiran',
				        	  nama : 'Nama Anak',
				        	  jenisKelamin : '-',
				        	  tanggalLahir : '1-1-2000',
				        	  tempatLahir : '-'
				          });
				          aktaKelahiranStore.insert(0, r);
				          aktaKelahiranGridEditing.startEdit(0, 0);
				          newRow = true;
					  }
				},
				{
					  text : 'Hapus',
					  iconCls : 'delete',
					  handler: function(){
						  postDeleteAktaKelahiran();
					  }
				},
				{
					  text : 'Cetak',
					  iconCls : 'cetakdk2',
					  handler: function(){
						  postCetakAktaKelahiran();
					  }
				}
            ]
      })
	});
	
	aktaKelahiranGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        aktaKelahiranStore.removeAt(0);
	    newRow = false;
	});
	
	aktaKelahiranGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    if(newRow == true){
	    	noAktaKelahiran = Ext.getCmp('noAktaKelahiranTextField').getValue();
	    	nama = Ext.getCmp('namaTextField').getValue();
	    	tanggalLahir = Ext.getCmp('tanggalLahirDateField').getValue();
	    	tempatLahir = Ext.getCmp('tempatLahirTextField').getValue();
	    	noAktaNikah = Ext.getCmp('aktaNikahComboBox').getValue();
	    	jenisKelamin = Ext.getCmp('jenisKelaminComboBox').getValue();
	    	noKK = Ext.getCmp('kkComboBox').getValue();
	    	jenisKelahiran = Ext.getCmp('jenisKelahiranComboBox').getValue();
	    	bantuanKelahiran = Ext.getCmp('bantuanKelahiranComboBox').getValue();
		    postAddAktaKelahiran();
		    newRow = false;
	    }
	    else{
	    	//postEditAgama(o.record.get('nama'), o.record.get('agamaId'));
	    }
	},this);
	
	aktaKelahiranGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			noAktaKelahiran = selectedRecord[0].data.noAktaKelahiran;
			nikAyah = selectedRecord[0].data.nikAyah;
			nikIbu = selectedRecord[0].data.nikibu;
		}
		catch(err){
			
		}
	});
	
	Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [
	    {
	        region: 'center',
	        layout:'anchor',
	        items:[
	               aktaKelahiranGrid
	        ]
	    }]
	});
	
});

function postAddAktaKelahiran(){
	Ext.Ajax.request({
        url: '../AktaKelahiranController',
        params: { 
        	'command' : 'add',
        	'noAktaKelahiran':noAktaKelahiran,
	    	'nama' : nama ,
	    	'tanggalLahir' : tanggalLahir,
	    	'tempatLahir' : tempatLahir,
	    	'noAktaNikah' : noAktaNikah,
	    	'jenisKelamin':jenisKelamin,
	    	'noKK' : noKK,
	    	'jenisKelahiran' : jenisKelahiran,
	    	'bantuanKelahiran' : bantuanKelahiran
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
        		aktaKelahiranStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteAktaKelahiran(){
	Ext.Ajax.request({
        url: '../AktaKelahiranController',
        params: { 
        	'command' : 'delete',
        	'noAktaKelahiran':noAktaKelahiran
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
        		aktaKelahiranStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postCetakAktaKelahiran(){
	Ext.Ajax.request({
        url: '../AktaKelahiranController',
        params: { 
        	'command' : 'print',
        	'noAktaKelahiran':noAktaKelahiran,
        	'nikAyah' : nikAyah,
        	'nikIbu' : nikIbu
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