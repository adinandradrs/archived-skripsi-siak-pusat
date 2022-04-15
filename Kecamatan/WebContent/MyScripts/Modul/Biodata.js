//GLOBAL VARIABEL
var cacatGrid;
var bioCacatGrid;
var bioPindahGrid;

var cacatStore;
var agamaStore;
var statusKawinStore;
var pendidikanStore;
var pekerjaanStore;
var kelurahanStore;
var bioCacatStore;
var pendudukStore;
var bioPindahStore;
var kotaStore;

var nik;
var cacatID;
var kotaID;
var aktif;
var biodataCacatID;

Ext.onReady(function(){
	var biodataMenu = Ext.create('Ext.menu.Menu', {
	    items: [{
	        text: 'Formulir',
	        handler:function(){
	        	callBiodataFormPanel();
	        }
	    },{
	        text: 'Penduduk Cacat',
	        handler:function(){
	        	callBiodataCacatGrid();
	        }
	    },{
	        text: 'Pindah Penduduk',
	        handler:function(){
	        	callBiodataPindahGrid();
	        }
	    }],
	    floating: false, 
	    anchor:'100% 100%'
	});
	
	Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [{
	    	title: 'Navigasi',
	    	collapsible:true,
	    	width:180,
	        region:'west',
	        layout:'anchor',
	        items:[biodataMenu]
	    }, {
	        region: 'center',
	        layout:'anchor',
	        id:'biodataPanel'
	    }]
	});
	
});

//LAYOUT
function doLayoutWindow(object){
	Ext.getCmp("biodataPanel").removeAll(true);
	Ext.getCmp("biodataPanel").add(object);
}

//FORM
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

function callBiodataFormPanel(){
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

	Ext.define('Cacat', {
	    extend: 'Ext.data.Model',
	    fields: [
	    {
	        name: 'cacatId', 
	        type: 'string'
	    },
	    {
	        name: 'nama',  
	        type: 'string'
	    }
	    ]
	});
	
	cacatStore = Ext.create('Ext.data.Store', {
	    model: 'Cacat',
	    pageSize: 12,
	    proxy: {
	        type: 'ajax',
	        url : '../BiodataController?command=load&select=cacat',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
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
	
	statusKawinStore = Ext.create('Ext.data.Store', {
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
	
	pendidikanStore = Ext.create('Ext.data.Store', {
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
	
	pekerjaanStore = Ext.create('Ext.data.Store', {
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
	
	agamaStore = Ext.create('Ext.data.Store', {
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
	        url : '../BiodataController?command=load&select=kelurahan',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	var sm = Ext.create('Ext.selection.CheckboxModel');
	cacatGrid = Ext.create('Ext.grid.Panel', {
	    selModel: sm,
	    store: cacatStore,
	    anchor:'100% 100%',
	    loadMask: true,
	    columns: [
	        { header: 'ID Cacat',  dataIndex: 'cacatId', hidden:true, hideable:false },
	        { header: 'Nama', dataIndex: 'nama'}
	    ],
	    forceFit : true,
	    tbar:[
	          {
	        	  text : 'Simpan',
	        	  handler:function(){
	        		  var selected = "";
	        		  var s = cacatGrid.getSelectionModel().getSelection();
	        		  Ext.each(s, function (item) {
	        			  selected+=item.data.cacatId+",";
	        		  });
	        		  if (selected.length == 0){
	        			  Ext.Msg.alert('Peringatan', 'Pilih salah satu data');
	        		  }
	        		  else{
	        			  postAddBiodataCacatForm(selected);
	        		  }
	        	  }
	          }
	         ]
	});
	
	var form = Ext.create('Ext.form.Panel', {
        url:'../BiodataController?command=add&type=biodata',
        frame:false,
        defaultType: 'textfield',
        bodyStyle:'padding:10px 30px 0',
        anchor:'100% 100%',
        defaults: {
            anchor: '80%'
        },
        items: [{
        	fieldLabel: 'Kelurahan',
            name: 'kelurahanComboBox',
            xtype:'combobox',
            editable:false,
            store:kelurahanStore,
            displayField: 'nama',
            valueField: 'kelurahanId'
        },{
            fieldLabel: 'Nama',
            name: 'namaTextField',
            allowBlank:false
        },{
            fieldLabel: 'Tanggal Lahir',
            name: 'tanggalLahirDateField',
            xtype : 'datefield',
            format : 'Y-m-d',
            editable:false
        },{
        	fieldLabel: 'Tempat Lahir',
        	name: 'tempatLahirTextField',
            allowBlank:false
        },{
        	fieldLabel: 'Kelamin',
        	name: 'kelaminComboBox',
            xtype:'combobox',
            editable:false,
            store: kelaminStore,
            displayField: 'text',
            valueField: 'text'
        },{
        	fieldLabel: 'Gol. Darah',
        	name: 'golonganDarahComboBox',
            xtype:'combobox',
            editable:false,
            store:golonganDarahStore,
            displayField: 'text',
            valueField: 'text'
        },
        {
        	fieldLabel: 'Agama',
        	name: 'agamaComboBox',
            xtype:'combobox',
            editable:false,
            store:agamaStore,
            displayField: 'nama',
            valueField: 'agamaId'
        },
        {
        	fieldLabel: 'Status kawin',
        	name: 'statusKawinComboBox',
            xtype:'combobox',
            editable:false,
            store:statusKawinStore,
            displayField: 'nama',
            valueField: 'hubunganDetailId'
        },
        {
        	fieldLabel: 'Pendidikan',
        	name: 'pendidikanComboBox',
            xtype:'combobox',
            editable:false,
            store:pendidikanStore,
            displayField: 'nama',
            valueField: 'pendidikanId'
        },
        {
        	fieldLabel: 'Pekerjaan',
        	name: 'pekerjaanComboBox',
            xtype:'combobox',
            editable:false,
            store:pekerjaanStore,
            displayField: 'nama',
            valueField: 'pekerjaanId'
        },
        {
        	fieldLabel: 'Akta Kelahiran',
        	name: 'aktaLahirTextField',
        	allowBlank:false
        },
        {
        	fieldLabel: 'Kartu Keluarga',
            name: 'kkTextField',
            allowBlank:false
        },
        {
        	fieldLabel: 'Telepon',
            name: 'teleponTextField',
            allowBlank : false
        },
        {
        	fieldLabel: 'Alamat',
        	xtype:'textareafield',
            name: 'alamatTextArea',
            allowBlank : false
        },
        {
        	fieldLabel: 'RT',
            name: 'rtTextField',
            allowBlank : false
        },
        {
        	fieldLabel: 'RW',
            name: 'rwTextField',
            allowBlank : false
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
            	var form = this.up('form').getForm();
            	form.submit({
            		success: function(form, action) {
            			Ext.Msg.alert('Success', "NIK : "+action.result.explain);
            		},
            		failure: function(form, action) {
            			Ext.Msg.alert('Failed', "NIK : "+action.result.explain);
            		}
            	}); 
            }
        }]
    });
	
	var biodataPanel = Ext.create('Ext.panel.Panel', {
	    anchor:'100% 100%',
	    layout: 'border',
	    items:[
	           {
	        	   xtype:'panel',
	        	   region:'center',
	        	   title:'Formulir',
	        	   items:[
	        	          	form
	        	         ],
	        	   layout:'anchor'
	           },
	           {
	        	   xtype:'panel',
	        	   region:'east',
	        	   title: 'Data Cacat',
	        	   collapsible:true,
	        	   width:240,
	        	   items:[
	        	          	cacatGrid
	        	   ]
	           }
	          ]
	});
	doLayoutWindow(biodataPanel);
}

function callBiodataCacatGrid(){
	Ext.define('Cacat', {
	    extend: 'Ext.data.Model',
	    fields: [
	    {
	        name: 'cacatId', 
	        type: 'string'
	    },
	    {
	        name: 'nama',  
	        type: 'string'
	    }
	    ]
	});
	
	cacatStore = Ext.create('Ext.data.Store', {
	    model: 'Cacat',
	    pageSize: 12,
	    proxy: {
	        type: 'ajax',
	        url : '../BiodataController?command=load&select=cacat',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	Ext.define('BioCacat', {
	    extend: 'Ext.data.Model',
	    fields: [
        {
        	name: 'biodataCacatId',
        	type: 'string'
        },
	    {
	    	name: 'nik',
	    	type: 'string'
	    },
	    {
	        name: 'cacatNama',  
	        type: 'string'
	    },
	    {
	        name: 'cacatId', 
	        type: 'string'
	    },
	    {
	    	name : 'aktif',
	    	type: 'bool'
	    }
	    ]
	});
	
	Ext.define('Penduduk',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'nik',
			type : 'string'
		}
        ]
	});
	
	pendudukStore = Ext.create('Ext.data.Store', {
		model : 'Penduduk',
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
	
	var groupFeature = Ext.create('Ext.grid.feature.Grouping',{
        groupHeaderTpl: '{name} ({rows.length} data)'
    });
	
	var newRow = false;
	
	var bioCacatGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	bioCacatStore = Ext.create('Ext.data.Store', {
	    model: 'BioCacat',
	    pageSize: 12,
	    groupField: 'nik',
	    proxy: {
	        type: 'ajax',
	        url : '../BiodataCacatController?command=load',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	bioCacatGrid = Ext.create('Ext.grid.Panel', {
	    title: 'Data Biodata Cacat',
	    store: bioCacatStore,
	    anchor:'100% 100%',
	    loadMask: true,
	    features: [groupFeature],
	    plugins: [bioCacatGridEditing],
	    columns: [
            { header: 'ID Biodata Cacat', dataIndex: 'biodataCacatId', hidden:true, hideable:false },
	        { header: 'ID Cacat', dataIndex: 'cacatId', hidden:true, hideable:false },
	        { header: 'NIK',  dataIndex: 'nik',editor:{
	        	id : 'pendudukComboBox',
	        	xtype:'combobox',
	        	store: pendudukStore,
	            displayField: 'nik',
	            valueField: 'nik',
	            editable : true,
	            queryMode: 'local'
	        }},
	        { header: 'Cacat', dataIndex: 'cacatNama', editor:{
	        	id : 'cacatComboBox',
	        	xtype:'combobox',
	        	store: cacatStore,
	            displayField: 'nama',
	            valueField: 'cacatId',
	            editable : false
	        }},
	        { header: 'Aktif', dataIndex: 'aktif', xtype:'checkcolumn', groupable:false, editor:{
	        	id : 'aktifCheckBox',
	        	xtype : 'checkbox'
	        }}
	    ],
	    forceFit : true,
	    tbar:[
	         {
	        	 text : 'Tambah',
	        	 iconCls : 'add',
	        	 handler:function(){
	        		 var r = Ext.create('BioCacat', {
	        			 nik : '',
	        			 cacatNama : 'Nama Cacat',
	        			 aktif:true
	                 });
	        		 bioCacatStore.insert(0, r);
	                 bioCacatGridEditing.startEdit(0, 0);
	                 newRow = true;
	        	 }
	         },
	         {
	        	 text : 'Delete',
	        	 iconCls : 'delete',
	        	 handler:function(){
	        		 postDeleteBiodataCacat();
	        	 }
	         }
	    ],
	    bbar :Ext.create('Ext.PagingToolbar', {
            store: bioCacatStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display"
	    })
	});
	
	bioCacatGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        bioCacatStore.removeAt(0);
	    newRow = false;
	});
	
	bioCacatGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    nik = Ext.getCmp("pendudukComboBox").getValue();
	    if(newRow == true){
	    	cacatID = Ext.getCmp("cacatComboBox").getValue();
	    	postAddBiodataCacat();
		    newRow = false;
	    }
	    else{
	    	cacatID = o.record.get("cacatId");
	    	aktif = o.record.get('aktif');
	    	postEditBiodataCacat(aktif);
	    }
	},this);
	
	bioCacatGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			biodataCacatID = selectedRecord[0].data.biodataCacatId;		
		}
		catch(err){
			
		}
	});
	
	doLayoutWindow(bioCacatGrid);
}

function callBiodataPindahGrid(){
	Ext.define('Kota',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'kotaId',
			type : 'string'
		},
		{
			name : 'nama',
			type : 'string'
		}
        ]
	});
	
	kotaStore = Ext.create('Ext.data.Store',{
		model : 'Kota',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../BiodataPindahController?command=load&select=kota',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	Ext.define('Penduduk',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'nik',
			type : 'string'
		}
        ]
	});
	
	pendudukStore = Ext.create('Ext.data.Store', {
		model : 'Penduduk',
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
	
	Ext.define('BiodataPindah',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'nik',
			type : 'string'
		},
		{
			name : 'namaPenduduk',
			type : 'string'
		},
		{
			name : 'kotaId',
			type : 'string'
		},
		{
			name : 'namaKota',
			type : 'string'
		},
		{
			name : 'tanggalPengajuan',
			type : 'string'
		}
        ]
	});
	
	bioPindahStore = Ext.create('Ext.data.Store', {
		model : 'BiodataPindah',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../BiodataPindahController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	var newRow = false;
	
	var bioPindahGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	bioPindahGrid = Ext.create('Ext.grid.Panel',{
		title: 'Data Penduduk Pindah',
	    store: bioPindahStore,
	    anchor:'100% 100%',
	    plugins : [bioPindahGridEditing],
	    columns : [
           {header: 'NIK', dataIndex: 'nik',editor:{
        	   	id : 'pendudukComboBox',
				xtype:'combobox',
				store: pendudukStore,
				displayField: 'nik',
				valueField: 'nik',
				editable : true,
				queryMode: 'local'
           }},
           {header: 'Nama', dataIndex: 'namaPenduduk'},
           {header: 'ID Kota', dataIndex: 'kotaId', hidden:true, hideable:false},
           {header: 'Nama Kota', dataIndex: 'namaKota', editor:{
        	   	id : 'kotaComboBox',
        	   	xtype: 'combobox',
        	   	store : kotaStore,
        	   	displayField : 'nama',
        	   	valueField: 'kotaId',
        	   	editable: false
           }},
           {header: 'Pengajuan', dataIndex: 'tanggalPengajuan'},
        ],
	    loadMask: true,
	    forceFit : true,
	    tbar:[
	          {
	        	  text:'Tambah',
	        	  iconCls : 'add',
	        	  handler:function(){
	        		  var r = Ext.create('BiodataPindah', {
	        			 nik : 'NIK Penduduk',
	        			 namaKota : 'Nama Kota' 
	        		  });
	        		  bioPindahStore.insert(0, r);
	        		  bioPindahGridEditing.startEdit(0, 0);
	        		  newRow = true;
	        	  }
        	  },
        	  {
	        	  text:'Hapus',
	        	  iconCls : 'delete',
	        	  handler:function(){
	        		  postDeleteBiodataPindah();
	        	  }
        	  }
	    ],
	    bbar :Ext.create('Ext.PagingToolbar', {
            store: bioPindahStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display"
	    })
	});
	
	bioPindahGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        bioPindahStore.removeAt(0);
	    newRow = false;
	});
	
	bioPindahGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    nik = Ext.getCmp("pendudukComboBox").getValue();
	    if(newRow == true){
	    	kotaID = Ext.getCmp("kotaComboBox").getValue();
	    	postAddBiodataPindah();
		    newRow = false;
	    }
	    else{
	    	
	    }
	},this);
	
	bioPindahGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			nik = selectedRecord[0].data.nik;
		}
		catch(err){
			
		}
	});
	
	doLayoutWindow(bioPindahGrid);
}

//POST TO SERVER
function postAddBiodataCacatForm(data){
	Ext.Ajax.request({
        url: '../BiodataController',
        params: { 
            "command" : "add",
            "type" : "cacat",
            "data" : data
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
        		Ext.MessageBox.show({
				  modal : false,
		          title: 'Peringatan',
		          msg: "Data berhasil ditambahkan",
		          buttons: Ext.MessageBox.OK,
		          icon: Ext.MessageBox.Informational
		      	});
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddBiodataCacat(){
	Ext.Ajax.request({
        url: '../BiodataCacatController',
        params: { 
            "command" : "add",
            "nik" : nik,
            "cacatID" : cacatID
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
        		bioCacatStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditBiodataCacat(aktif){
	Ext.Ajax.request({
        url: '../BiodataCacatController',
        params: { 
            "command" : "edit",
            "biodataCacatID" : biodataCacatID,
            "aktif" : aktif,
            "cacatID" : cacatID,
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
        		bioCacatStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteBiodataCacat(){
	Ext.Ajax.request({
        url: '../BiodataCacatController',
        params: { 
            "command" : "delete",
            "biodataCacatID" : biodataCacatID
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
        		bioCacatStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddBiodataPindah(){
	Ext.Ajax.request({
        url: '../BiodataPindahController',
        params: { 
            "command" : "add",
            "nik" : nik,
            "kotaID" : kotaID
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
        		bioPindahStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteBiodataPindah(){
	Ext.Ajax.request({
        url: '../BiodataPindahController',
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
        		bioPindahStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}