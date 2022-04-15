//GLOBAL VARIABEL
var bioGrid;

var bioStore;
var kelurahanStore;

var kelurahanID;
var nik;
var nama;
var tanggalLahir;
var tempatLahir;
var kelamin;
var golonganDarah;
var agama;
var statusKawin;
var pendidikan;
var pekerjaan;
var kartuKeluarga;
var aktaKelahiran;
var telepon;
var alamat;
var rt;
var rw;

Ext.onReady(function(){
	
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
			name : 'aktaNikah',
			type : 'string'
		},
		{
			name : 'agamaId',
			type : 'string'
		},
		{
			name : 'statusKawin',
			type : 'string'
		},
		{
			name : 'pendidikanId',
			type : 'string'
		},
		{
			name : 'pekerjaanId',
			type : 'string'
		},
		{
			name : 'noKK',
			type : 'string'
		},
		{
			name : 'telepon',
			type : 'string'
		},
		{
			name : 'alamat',
			type : 'string'
		},{
			name : 'rt',
			type : 'string'
		},
		{
			name:'rw',
			type:'string'
		},
		{
			name : 'kelurahanId',
			type: 'string'
		}
        ]
	});
	
	bioStore = Ext.create('Ext.data.Store', {
		model : 'Biodata',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../PendudukController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	bioGrid = Ext.create('Ext.grid.Panel',{
		store: bioStore,
	    anchor:'100% 100%',
	    columns: [
            {header:'NIK', dataIndex:'nik'},
            {header:'Nama', dataIndex:'nama'},
            {header:'Jenis Kelamin', dataIndex:'jenisKelamin'},
            {header:'Tanggal Lahir', dataIndex:'tanggalLahir'},
            {header:'Tempat Lahir', dataIndex:'tempatLahir'}
	    ],
	    plugins: [{
            ptype: 'rowexpander',
            rowBodyTpl : [
                '<div style="float:left"><b>Pendidikan:</b> {pendidikan}<br>',
                '<b>Pekerjaan:</b> {pekerjaan}<br>',
                '<b>Akta Kelahiran:</b> {aktaKelahiran}<br>',
                '<b>No. KK:</b> {kartuKeluarga}<br>',
                '<b>Agama:</b> {agama}<br>',
                '<b>Golongan Darah:</b> {golonganDarah}<br></div>',
                '<div style="float:left; margin-left:70px;">{foto}<br>'+
                '<center><b>Akta Nikah :</b> {aktaNikah}<center></div>'
            ]
        }],
	    forceFit : true,
	    tbar:[
	          {
	        	  text : 'Ubah',
	        	  iconCls : 'edit',
	        	  handler:function(){
	        		  callEditPendudukWindow();
	        	  }
	          },
	          {
	        	  text : 'Delete',
	        	  iconCls : 'delete',
	        	  handler:function(){
	        		  postDeletePenduduk();
	        	  }
	          }
	    ],
	    bbar:Ext.create('Ext.PagingToolbar', {
            store: bioStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display"
	    })
	});
	
	bioGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			nik = selectedRecord[0].data.nik;
			nama = selectedRecord[0].data.nama;
			tanggalLahir = selectedRecord[0].data.tanggalLahir;
			tempatLahir = selectedRecord[0].data.tempatLahir;
			kelamin = selectedRecord[0].data.jenisKelamin;
			golonganDarah = selectedRecord[0].data.golonganDarah;
			agama = selectedRecord[0].data.agamaId;
			statusKawin = selectedRecord[0].data.statusKawin;
			pendidikan = selectedRecord[0].data.pendidikanId;
			pekerjaan = selectedRecord[0].data.pekerjaanId;
			aktaKelahiran = selectedRecord[0].data.aktaKelahiran;
			kartuKeluarga = selectedRecord[0].data.noKK;
			telepon = selectedRecord[0].data.telepon;
			alamat = selectedRecord[0].data.alamat;
			rt = selectedRecord[0].data.rt;
			rw = selectedRecord[0].data.rw;
			kelurahanID = selectedRecord[0].data.kelurahanId;
		}
		catch(err){
			
		}
	});
	
	Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [{
	        region: 'center',
	        layout:'anchor',
	        id:'pendudukPanel',
	        items:[
	               bioGrid
	              ]
	    }]
	});
	
});

function callEditPendudukWindow(){	
	/*****************************/
	var kelaminStore = Ext.create('Ext.data.Store', {
	    fields: ['text'],
	    data : [
	        {"text":"Laki-Laki"},
	        {"text":"Perempuan"}
	    ]
	});
	
	var golonganDarahStore = Ext.create('Ext.data.Store', {
	    fields: ['text'],
	    data : [
	            {"text":"A"},
	            {"text":"AB"},
	            {"text":"B"},
	        	{"text":"O"},
		        {"text":"A+"},
		        {"text":"A-"},
		        {"text":"B+"},
		        {"text":"B-"},
		        {"text":"AB+"},
		        {"text":"AB-"},
		        {"text":"O+"},
		        {"text":"A-"},
		        {"text":"-"}
	    ]
	});
	/*****************************/
	
	Ext.define('StatusKawin', {
	    extend: 'Ext.data.Model',
	    fields: [
	    {
	        name: 'hubunganDetailId', 
	        type: 'string'
	    },
	    {
	        name: 'nama',  
	        type: 'string'
	    }
	    ]
	});
	
	var statusKawinStore = Ext.create('Ext.data.Store', {
	    model: 'StatusKawin',
	    pageSize: 12,
	    proxy: {
	        type: 'ajax',
	        url : '../BiodataController?command=load&select=statusKawin',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	Ext.define('Pendidikan', {
	    extend: 'Ext.data.Model',
	    fields: [
	    {
	        name: 'pendidikanId', 
	        type: 'string'
	    },
	    {
	        name: 'nama',  
	        type: 'string'
	    }
	    ]
	});
	
	var pendidikanStore = Ext.create('Ext.data.Store', {
	    model: 'Pendidikan',
	    pageSize: 12,
	    proxy: {
	        type: 'ajax',
	        url : '../BiodataController?command=load&select=pendidikan',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	Ext.define('Pekerjaan', {
	    extend: 'Ext.data.Model',
	    fields: [
	    {
	        name: 'pekerjaanId', 
	        type: 'string'
	    },
	    {
	        name: 'nama',  
	        type: 'string'
	    }
	    ]
	});
	
	var pekerjaanStore = Ext.create('Ext.data.Store', {
	    model: 'Pekerjaan',
	    proxy: {
	        type: 'ajax',
	        url : '../BiodataController?command=load&select=pekerjaan',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	
	Ext.define('Agama', {
	    extend: 'Ext.data.Model',
	    fields: [
	    {
	        name: 'agamaId', 
	        type: 'string'
	    },
	    {
	        name: 'nama',  
	        type: 'string'
	    }
	    ]
	});
	
	var agamaStore = Ext.create('Ext.data.Store', {
	    model: 'Agama',
	    pageSize: 12,
	    proxy: {
	        type: 'ajax',
	        url : '../BiodataController?command=load&select=agama',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	Ext.define('Kecamatan', {
	    extend: 'Ext.data.Model',
	    fields: [
	    {
	        name: 'kecamatanId', 
	        type: 'string'
	    },
	    {
	        name: 'nama',  
	        type: 'string'
	    }
	    ]
	});
	
	var kecamatanStore = Ext.create('Ext.data.Store', {
	    model: 'Kecamatan',
	    pageSize: 12,
	    proxy: {
	        type: 'ajax',
	        url : '../WilayahController?command=load&select=kecamatan',
	        reader: {
	            type: 'json'
	        }
	    }
	});
	
	Ext.define('Kelurahan', {
	    extend: 'Ext.data.Model',
	    fields: [
	    {
	        name: 'kelurahanId', 
	        type: 'string'
	    },
	    {
	        name: 'nama',  
	        type: 'string'
	    }
	    ]
	});
	
	kelurahanStore = Ext.create('Ext.data.Store', {
	    model: 'Kelurahan',
	    pageSize: 12,
	    proxy: {
	        type: 'ajax',
	        url : '../WilayahController?command=load&select=kelurahan',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad:true
	});

	var form = Ext.create('Ext.form.Panel', {
        frame:false,
        defaultType: 'textfield',
        bodyStyle:'padding:10px 20px 0',
        anchor:'100% 100%',
        defaults: {
            anchor: '100%'
        },
        items: [
		{
			fieldLabel: 'Kecamatan',
		    name: 'kecamatanComboBox',
		    id:'kecamatanComboBox',
		    xtype:'combobox',
		    editable:false,
		    store:kecamatanStore,
		    displayField: 'nama',
		    valueField: 'kecamatanId',
		    listeners:{
		         scope: this,
		         'select': function(x,r){
			        	 Ext.Ajax.request({
			     	        url: '../WilayahController',
			     	        params: { 
			     	            "command" : "fetch",
			     	            "kecamatanID" : r[0].data.kecamatanId
			     	        }
			     		 }
			        	 );
			        	 Ext.getCmp('kelurahanComboBox').setValue('');
		         }
		    }
		},
        {
        	fieldLabel: 'Kelurahan',
            name: 'kelurahanComboBox',
            id:'kelurahanComboBox',
            xtype:'combobox',
            editable:false,
            loadMask:false,
            store:kelurahanStore,
            displayField: 'nama',
            valueField: 'kelurahanId',
            listeners:{
		         scope: this,
		         'expand' : function(y){
		        	 kelurahanStore.load(function(records, operation, success) {
		        	 });
		         }
	         }
        },{
            fieldLabel: 'Nama',
            name: 'namaTextField',
            id: 'namaTextField',
            allowBlank:false,
            value:nama
        },{
            fieldLabel: 'Tanggal Lahir',
            name: 'tanggalLahirDateField',
            id:'tanggalLahirDateField',
            xtype : 'datefield',
            format : 'd-m-Y',
            editable:false,
            value:tanggalLahir
        },{
        	fieldLabel: 'Tempat Lahir',
        	name: 'tempatLahirTextField',
        	id:'tempatLahirTextField',
            allowBlank:false,
            value:tempatLahir
        },{
        	fieldLabel: 'Kelamin',
        	name: 'kelaminComboBox',
        	id:'kelaminComboBox',
            xtype:'combobox',
            editable:false,
            store: kelaminStore,
            displayField: 'text',
            valueField: 'text',
            value:kelamin
        },{
        	fieldLabel: 'Gol. Darah',
        	name: 'golonganDarahComboBox',
            xtype:'combobox',
            id:'golonganDarahComboBox',
            editable:false,
            store:golonganDarahStore,
            displayField: 'text',
            valueField: 'text',
            value:golonganDarah
        },
        {
            xtype:'tabpanel',
            plain:true,
            activeTab: 0,
            height:235,
            defaults:{bodyStyle:'padding:10px'},
            items:[
                   {
	       				title:'Data Pokok',
	    				defaults: {anchor: '100%'},
	    				defaultType: 'textfield',
	    				items:[
	    				    {
	    				    	fieldLabel: 'Agama',
	    		            	name: 'agamaComboBox',
	    		                id:'agamaComboBox',
	    		            	xtype:'combobox',
	    		                editable:false,
	    		                store:agamaStore,
	    		                displayField: 'nama',
	    		                valueField: 'agamaId',
	    		                value:agama
	    					},
	    					{
	    						fieldLabel: 'Status kawin',
	                       		name: 'statusKawinComboBox',
	                       		id:'statusKawinComboBox',
	                       		xtype:'combobox',
	                       		value:statusKawin,
	                       		editable:false,
	                       		store:statusKawinStore,
	                       		displayField: 'nama',
	                       		valueField: 'hubunganDetailId'
	    					},
	    					{
	    						fieldLabel: 'Pendidikan',
	                       		name: 'pendidikanComboBox',
	                       		id:'pendidikanComboBox',
	                       		value:pendidikan,
	                       		xtype:'combobox',
	                       		editable:false,
	                       		store:pendidikanStore,
	                       		displayField: 'nama',
	                       		valueField: 'pendidikanId'
	    					},
	    					{
	    						fieldLabel: 'Pekerjaan',
	                       		name: 'pekerjaanComboBox',
	                       		id:'pekerjaanComboBox',
	                       		value:pekerjaan,
	                       		xtype:'combobox',
	                       		editable:false,
	                       		store:pekerjaanStore,
	                       		displayField: 'nama',
	                       		valueField: 'pekerjaanId'
	    					}
	    				]
                   },
                   {
                	   	title:'Keterangan',
	    				defaults: {width: 230},
	    				defaultType: 'textfield',
	    				items:[
	    				       	{
						        	fieldLabel: 'Akta Kelahiran',
						        	id:'aktaLahirTextField',
						        	name: 'aktaLahirTextField',
						        	value:aktaKelahiran,
						        	allowBlank:false
						        },
						        {
						        	fieldLabel: 'Kartu Keluarga',
						            name: 'kkTextField',
						            id:'kkTextField',
						            value:kartuKeluarga,
						            allowBlank:false
						        },
	    				       	{
						        	fieldLabel: 'Telepon',
						            name: 'teleponTextField',
						            id:'teleponTextField',
						            value:telepon,
						            allowBlank : false
						        },
						        {
						        	fieldLabel: 'Alamat',
						        	xtype:'textareafield',
						            name: 'alamatTextArea',
						            id:'alamatTextArea',
						            allowBlank : false,
						            value:alamat
						        },
						        {
						        	fieldLabel: 'RT',
						            name: 'rtTextField',
						            allowBlank : false,
						            id:'rtTextField',
						            value:rt
						        },
						        {
						        	fieldLabel: 'RW',
						            name: 'rwTextField',
						            allowBlank : false,
						            id:'rwTextField',
						            value:rw
						        }
	    				
	    				]
                   }
                  ]
        }
        ],
        buttons: [{
            text: 'Foto',
            handler:function(){
            	callPhotoWindow();
            }
        },{
            text: 'Save',
            handler:function(){
            	postEditPenduduk();
            }
        }]
    });
	
	
	Ext.create('Ext.window.Window', {
	    title: 'Ubah Data Penduduk',
	    height: 500,
	    width: 600,
	    layout: 'anchor',
	    items: [form]  
	}).show();
}

function callPhotoWindow(){
	var fotoForm = Ext.create('Ext.form.Panel', {
	    items: [{
	        xtype: 'filefield',
	        name: 'photo',
	        fieldLabel: 'Photo',
	        msgTarget: 'side',
	        allowBlank: false,
	        anchor: '100%',
	        buttonText: 'Pilih Foto...'
	    }],

	    buttons: [{
	        text: 'Upload',
	        handler: function() {
	            var form = this.up('form').getForm();
	            if(form.isValid()){
	                form.submit({
	                    url: '../BiodataController?command=upload',
	                    waitMsg: 'Upload foto penduduk...',
	                    success: function(fp, o) {
	                        Ext.Msg.alert('Success', 'Foto telah tersimpan sementara');
	                    }
	                });
	            }
	        }
	    }]
	});
	
	var panel = Ext.widget('window', {
        title: 'Foto',
        closeAction: 'destroy',
        width: 400,
        layout:'anchor',
        items:[
               	fotoForm
              ],
        modal:false
    });
    panel.setPosition(50,50,true);
    panel.show();
}

function postEditPenduduk(){
	Ext.Ajax.request({
        url: '../PendudukController',
        params: { 
            "command" : "edit",
            "nik" : nik,
            "nama" : Ext.getCmp('namaTextField').getValue(),
	    	"agama" : Ext.getCmp('agamaComboBox').getValue(),
	    	"tanggalLahir" : Ext.getCmp('tanggalLahirDateField').getValue(),
	    	"tempatLahir" : Ext.getCmp('tempatLahirTextField').getValue(),
	    	"golonganDarah" : Ext.getCmp('golonganDarahComboBox').getValue(),
	    	"statusKawin" : Ext.getCmp('statusKawinComboBox').getValue(),
	    	"pendidikan" : Ext.getCmp('pendidikanComboBox').getValue(),
	    	"pekerjaan" : Ext.getCmp('pekerjaanComboBox').getValue(),
	    	"jenisKelamin" : Ext.getCmp('kelaminComboBox').getValue(),
	    	"aktaKelahiran" : Ext.getCmp('aktaLahirTextField').getValue(),
	    	"kartuKeluarga" : Ext.getCmp('kkTextField').getValue(),
	    	"telepon" : Ext.getCmp('teleponTextField').getValue(),
	    	"alamat" : Ext.getCmp('alamatTextArea').getValue(),
	    	"rt" : Ext.getCmp('rtTextField').getValue(),
	    	"rw" : Ext.getCmp('rwTextField').getValue(),
	    	"kelurahanID" : Ext.getCmp('kelurahanComboBox').getValue()
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
        		bioStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeletePenduduk(){
	Ext.Ajax.request({
        url: '../PendudukController',
        params: { 
            "command" : "delete",
            "nik" : nik
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
        		bioStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}
