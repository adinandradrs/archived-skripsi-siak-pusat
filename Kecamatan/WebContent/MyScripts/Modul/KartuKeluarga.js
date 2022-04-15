var ktpStore;
var bioStore;
var statusHubunganStore;
var kkDetailAtasStore;
var kkDetailBawahStore;
var kkStore;

var ktpGrid;
var bioGrid;
var kkDetailAtasGrid;
var kkDetailBawahGrid;
var kkGrid;

var step;
var nik;
var niks;
var kelurahanID;

var jenisKelamin;
var agamaID;
var pendidikanID;
var pekerjaanID;
var statusKawin;
var tempatLahir;
var tanggalLahir;

var noKK;
var kkDetailID;

var jenisCetak;

/*************************/
var encode;
var local;
var filters;
/*************************/

Ext.onReady(function(){
	
	encode = false;
	local = true;
	filters = {
        ftype: 'filters',
        encode: encode, 
        local:local,
        filters: [{
            type: 'boolean',
            dataIndex: 'visible'
        }]
    };
	
	var kkMenu = Ext.create('Ext.menu.Menu', {
	    items: [
        {
	        text: 'KK - Baru',
	        handler:function(){
	        	callKTPGrid();
	        }
	    },
	    {
	    	text: 'KK - Data',
	    	handler:function(){
	    		callKartuKeluargaGrid();
	    	}
	    }
        ],
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
	        items:[
	               kkMenu
            ]
	    }, {
	        region: 'center',
	        layout:'anchor',
	        id:'kartuKeluargaPanel'
	    }]
	});
	
});

function doLayoutWindow(object){
	Ext.getCmp('kartuKeluargaPanel').removeAll();
	Ext.getCmp('kartuKeluargaPanel').add(object);
}

function callKTPGrid(){
	Ext.define('ktp',{
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
			name : 'kelurahanId',
			type : 'string'
		}
        ]
	});
	
	ktpStore = Ext.create('Ext.data.Store', {
		model : 'ktp',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../KtpController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	ktpGrid = Ext.create('Ext.grid.Panel',{
		title:'<center>1. Pilih Pemohon/Kepala Keluarga</center>',
		store: ktpStore,
	    anchor:'100% 100%',
	    columns: [
            {header:'NIK', dataIndex:'nik', id:'nik',filter: {
	            type: 'string'   
            }},
            {header:'Nama', dataIndex:'nama'}
	    ],
	    forceFit : true,
	    features: [filters],
	    tbar : Ext.create('Ext.PagingToolbar', {
            store: ktpStore,
            displayInfo: true,
            displayMsg: 'Data KTP {0} - {1} dari {2}',
            emptyMsg: "Tidak terdapat data",
            items:[
                   {
                	   text:'Pilih Penduduk',
                	   handler:function(){
                		   if(kelurahanID == null && nik == null){
                			   Ext.MessageBox.show({
     	        				  modal : true,
     	        		          title: 'Peringatan',
     	        		          msg: 'Silakan pilih salah satu data yang terdapat pada tabel',
     	        		          buttons: Ext.MessageBox.OK,
     	        		          icon: Ext.MessageBox.ERROR
     	        		      });
                		   }
                		   else{                			   
                			   postAddKartuKeluarga();
                		   }
                	   }
                   }
            ]
	    
        })
	});
	
	ktpGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			kelurahanID = selectedRecord[0].data.kelurahanId;
			nik = selectedRecord[0].data.nik;
		}
		catch(e){
			
		}
	});
	
	doLayoutWindow(ktpGrid);
}

function callDetailKKPanel(by){
	nik = null;
	var toolBar = Ext.create('Ext.panel.Panel', {
	    anchor:'100%',
	    tbar: [{
	    	xtype: 'buttongroup',
	        columns: 3,
	        title: 'Pilihan',
	        collapsible:true,
	        items: [
				{
	        		text: 'Tambah',
	        		scale: 'medium',
	        		iconCls: 'add',
	                iconAlign: 'top',
	                handler: function(){
	                	if(nik != null){
	                		callFamilyRoleDialog(by);
		                	var sm = bioGrid.getSelectionModel();
		                	bioStore.remove(sm.getSelection());
	                	}
	                }
	        	},
	        	{
					text: 'Hapus',
					scale: 'medium',
					iconCls: 'delete',
				    iconAlign: 'top',
				    handler: function(){
				    	if(kkDetailID != null){
				    		postDeleteKartuKeluargaDetail();
				    	}
				    }
				},
				{
					text: 'Cetak',
					scale: 'medium',
					iconCls: 'cetak',
				    iconAlign: 'top',
				    handler: function(){
				    	if(by === "session"){
				    		postCetakKartuKeluargaDetail();
				    	}
				    	else{
				    		callCetakDialog();
				    	}
				    }
				}
	        ]
	    }]
	});
	
	Ext.define('kkDetailAtas',{
		extend : 'Ext.data.Model',
		fields :[
		{
			name : 'kkDetailId',
			type : 'string'
		},
		{
			name : 'nama',
			type : 'string'
		},
		{
			name : 'niks',
			type : 'string'
		},
		{
			name : 'jenisKelamin',
			type : 'string'
		},
		{
			name : 'tempatLahir',
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
			name : 'pendidikan',
			type : 'string'
		},
		{
			name : 'pekerjaan',
			type : 'string'
		}
        ]
	});
	
	kkDetailAtasStore = Ext.create('Ext.data.Store',{
		model : 'kkDetailAtas',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../KartuKeluargaDetailController?command=load&type=atas&by='+by,
	        reader: {
	            type: 'json'
	        }
		},
		autoLoad : true
	});
	
	kkDetailAtasGrid  = Ext.create('Ext.grid.Panel',{
		store: kkDetailAtasStore,
	    anchor:'100% 40%',
	    columns: [
            {header:'Nama Lengkap', dataIndex:'nama'},
            {header:'NIK', dataIndex:'niks'},
            {header:'Jenis Kelamin', dataIndex:'jenisKelamin'},
            {header:'Tanggal Lahir', dataIndex:'tanggalLahir'},
            {header:'Tempat Lahir', dataIndex:'tempatLahir'},
            {header:'Tanggal Lahir', dataIndex:'tanggalLahir'},
            {header:'Agama', dataIndex:'agama'},
            {header:'Pendidikan', dataIndex:'pendidikan'},
            {header:'Jenis Pekerjaan', dataIndex:'pekerjaan'}
	    ],
	    forceFit : true,
	    bbar: Ext.create('Ext.PagingToolbar', {
            store: kkDetailAtasStore,
            displayInfo: true,
            displayMsg: 'Data penduduk {0} - {1} dari {2}',
            emptyMsg: "Tidak terdapat data"
        })
	});
	
	kkDetailAtasGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			kkDetailID = selectedRecord[0].data.kkDetailId;
		}
		catch(e){
			
		}
	});
	
	Ext.define('kkDetailBawah',{
		extend : 'Ext.data.Model',
		fields :[
		{
			name : 'kkDetailId',
			type : 'string'
		},
		{
			name : 'statusKawin',
			type : 'string'
		},
		{
			name : 'statusHubungan',
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
			name : 'kewarganegaraan',
			type : 'string'
		},
		{
			name : 'noPaspor',
			type : 'string'
		},
		{
			name : 'noKitas',
			type : 'string'
		}
        ]
	});
	
	kkDetailBawahStore = Ext.create('Ext.data.Store',{
		model : 'kkDetailBawah',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../KartuKeluargaDetailController?command=load&type=bawah&by='+by,
	        reader: {
	            type: 'json'
	        }
		},
		autoLoad : true
	});
	
	kkDetailBawahGrid  = Ext.create('Ext.grid.Panel',{
		store: kkDetailBawahStore,
	    anchor:'100% 45%',
	    columns: [
            {header:'Status Perkawinan', dataIndex:'statusKawin'},
            {header:'Status Hubungan', dataIndex:'statusHubungan'},
            {header:'Kewarganegaraan', dataIndex:'kewarganegaraan'},
            {header:'No. Paspor', dataIndex:'noPaspor'},
            {header:'No. Kitas', dataIndex:'noKitas'},
            {header:'Ayah', dataIndex:'namaAyah'},
            {header:'Ibu', dataIndex:'namaIbu'}
	    ],
	    forceFit : true,
	    bbar: Ext.create('Ext.PagingToolbar', {
            store: kkDetailBawahStore,
            displayInfo: true,
            displayMsg: 'Data penduduk {0} - {1} dari {2}',
            emptyMsg: "Tidak terdapat data"
        })
	});
	
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
			name : 'kelurahanId',
			type : 'string'
		},
		{
			name : 'agamaId',
			type : 'string'
		},
		{
			name : 'pekerjaanId',
			type : 'string'
		},
		{
			name : 'pendidikanId',
			type : 'string'
		},
		{
			name : 'statusKawin',
			type : 'string'
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
	    anchor:'100% -74',
	    columns: [
            {header:'NIK', dataIndex:'nik',filter: {
	            type: 'string'   
            }},
            {header:'Nama', dataIndex:'nama'},
            {header:'Jenis Kelamin', dataIndex:'jenisKelamin'},
            {header:'Tanggal Lahir', dataIndex:'tanggalLahir'},
            {header:'Tempat Lahir', dataIndex:'tempatLahir'}
	    ],
	    features:[filters],
	    plugins: [{
            ptype: 'rowexpander',
            rowBodyTpl : [
                '<div style="float:left"><b>Pendidikan:</b> {pendidikan}<br>',
                '<b>Pekerjaan:</b> {pekerjaan}<br>',
                '<b>Akta Kelahiran:</b> {aktaKelahiran}<br>',
                '<b>No. KK:</b> {kartuKeluarga}<br>',
                '<b>Agama:</b> {agama}<br>',
                '<b>Golongan Darah:</b> {golonganDarah}<br></div>',
                '<div style="float:left; margin-left:70px;">{foto}</div>'
            ]
        }],
	    forceFit : true,
	    bbar: Ext.create('Ext.PagingToolbar', {
            store: bioStore,
            displayInfo: true,
            displayMsg: 'Data penduduk {0} - {1} dari {2}',
            emptyMsg: "Tidak terdapat data"
        })
	});
	
	bioGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			nik = selectedRecord[0].data.nik;
			
			tanggalLahir = selectedRecord[0].data.tanggalLahir;
			tempatLahir = selectedRecord[0].data.tempatLahir;
			agamaID = selectedRecord[0].data.agamaId;
			pekerjaanID = selectedRecord[0].data.pekerjaanId;
			pendidikanID = selectedRecord[0].data.pendidikanId;
			jenisKelamin = selectedRecord[0].data.jenisKelamin;
			statusKawin = selectedRecord[0].data.statusKawin;
			
		}
		catch(e){
			
		}
	});
	
	var myTabPanel = Ext.createWidget('tabpanel', {
		anchor:'100% 100%',
		activeTab: 0,
		items:[
		       {
		    	   title: 'Penduduk',
		    	   layout:'anchor',
		    	   items:[
		    	          	bioGrid
		    	   ]
		       },
		       {
		    	   title: 'Struktur',
		    	   layout:'anchor',
		    	   items:[
		    	          	kkDetailAtasGrid,
		    	          	kkDetailBawahGrid
	    	       ]
		       }
	    ]
	});
	
	var myPanel = Ext.create('Ext.panel.Panel',{
		title: '<center>2. Pilih Anggota Keluarga</center>',
		anchor:'100% 100%',
		layout:'anchor',
		items:[
		       toolBar,
		       myTabPanel
	    ]
	});
	
	doLayoutWindow(myPanel);
}

function callFamilyRoleDialog(by){
	Ext.define('StatusHubungan', {
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
	
	statusHubunganStore = Ext.create('Ext.data.Store', {
	    model: 'StatusHubungan',
	    pageSize: 12,
	    proxy: {
	        type: 'ajax',
	        url : '../BiodataController?command=load&select=statusHubungan',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	var myForm = Ext.create('Ext.form.Panel', {
        url:'../KartuKeluargaDetailController?command=add&by='+by,
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        anchor:'100% 100%',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 75
        },
        defaultType: 'textfield',
        defaults: {
            anchor: '100%'
        },

        items: [{
            fieldLabel: 'NIK',
            name: 'nikTextField',
            id : 'nikTextField',
            value : nik,
            readOnly:true,
            allowBlank : false
        },{
            fieldLabel: 'NIK Ayah',
            id : 'nikAyahTextField',
            name: 'nikAyahTextField',
            allowBlank : false,
            listeners: {
                'specialkey' : function(field, e){
                    if(e.getKey() == e.ENTER){
                    	var nikRef = Ext.getCmp('nikAyahTextField').getValue();
                    	var target = Ext.getCmp('namaAyahTextField');
                    	if(nikRef != Ext.getCmp('nikTextField').getValue() && nikRef!=Ext.getCmp('nikIbuTextField').getValue())
                    		postSearchByNIK(target, nikRef);
                    	else
                    		alert('Data pencarian tidak valid');
                    }
                }
            }
        },{
            fieldLabel: 'Nama Ayah',
            name: 'namaAyahTextField',
            id:'namaAyahTextField',
            readOnly:true,
            allowBlank : false
        }, 
        {
            fieldLabel: 'NIK Ibu',
            id : 'nikIbuTextField',
            name: 'nikIbuTextField',
            allowBlank : false,
            listeners: {
                'specialkey' : function(field, e){
                    if(e.getKey() == e.ENTER){
                    	var nikRef = Ext.getCmp('nikIbuTextField').getValue();
                    	var target = Ext.getCmp('namaIbuTextField');
                    	if(nikRef != Ext.getCmp('nikTextField').getValue() && nikRef!=Ext.getCmp('nikAyahTextField').getValue())
                    		postSearchByNIK(target, nikRef);
                    	else
                    		alert('Data pencarian tidak valid');
                    }
                }
            }
        },
        {
            fieldLabel: 'Nama Ibu',
            name: 'namaIbuTextField',
            id: 'namaIbuTextField',
            readOnly:true,
            allowBlank : false
        },
        {
            fieldLabel: 'Hubungan Keluarga',
            xtype:'combobox',
            name: 'statusHubunganComboBox',
            store:statusHubunganStore,
            displayField: 'nama',
            valueField: 'hubunganDetailId',
            editable: false,
            allowBlank : false
        }],

        buttons: [{
            text: 'Save',
            handler:function(){
            	var form = this.up('form').getForm();
                if (form.isValid()) {
                    form.submit({
                    	params:{
                    		'jenisKelamin' : jenisKelamin,
                    		'tempatLahir' : tempatLahir,
                    		'agamaID' : agamaID,
                    		'pekerjaanID' : pekerjaanID,
                    		'pendidikanID' : pendidikanID,
                    		'statusKawin' : statusKawin,
                    		'tanggalLahir' : tanggalLahir
                    	},
                        success: function(form, action) {
                        	kkDetailAtasStore.load(function(records, operation, success) {
                        	});
                        	kkDetailBawahStore.load(function(records, operation, success) {
                        	});
                        	myWindow.close();
                        },
                        failure: function(form, action) {
                        	kkDetailAtasStore.load(function(records, operation, success) {
                            });
                        	kkDetailBawahStore.load(function(records, operation, success) {
                            });
                        	myWindow.close();
                        }
                    });
                }
            }
        },{
            text: 'Cancel',
            handler:function(){
            	myWindow.close();
            }
        }]
    });
	
	var myWindow = Ext.create('Ext.window.Window',{
		title : 'Role Keluarga',
		width:300,
		height:250,
		layout:'anchor',
		items:[
		       myForm
	    ]
	}).show();
}

function callKartuKeluargaGrid(){
	Ext.define('kartuKeluarga',{
		extend : 'Ext.data.Model',
		fields :[
		{
			name : 'noKk',
			type : 'string'
		},
		{
			name : 'nik',
			type : 'string'
		},
		{
			name : 'nama',
			type : 'string'
		},
		{
			name : 'jenisBuat',
			type : 'string'
		},
		{
			name : 'tanggalBuat',
			type : 'string'
		}
        ]
	});
	
	kkStore = Ext.create('Ext.data.Store',{
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
	
	kkGrid = Ext.create('Ext.grid.Panel',{
		title : '<center>1. Pilih Data Kepala Keluarga</center>',
		store: kkStore,
	    anchor:'100% 100%',
	    columns: [
            {header:'No. KK', dataIndex:'noKk',filter: {
	            type: 'string'   
            }},
            {header:'NIK', dataIndex:'nik'},
            {header:'Nama Pemohon', dataIndex:'nama'},
            {header:'Jenis Buat', dataIndex:'jenisBuat'},
            {header:'Tanggal Buat', dataIndex:'tanggalBuat'}
	    ],
	    features:[filters],
	    forceFit : true,
	    tbar: [
	           { 
	        	   xtype: 'button', 
	        	   text: 'Detail', 
	        	   iconCls:'detail',
	        	   handler :function(){
	        		   var param = 'qstring&noKK='+noKK;
	        		   callDetailKKPanel(param);
	        	   }
    		   },
			   {
				   xtype: 'button', 
        		   text: 'Hapus', 
        		   iconCls:'delete',
        		   handler: function(){
        			   postDeleteKartuKeluarga();
        		   }
			   }
        ],
	    bbar: Ext.create('Ext.PagingToolbar', {
            store: kkStore,
            displayInfo: true,
            displayMsg: 'Data penduduk {0} - {1} dari {2}',
            emptyMsg: "Tidak terdapat data"
        })
	});
	
	kkGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			noKK = selectedRecord[0].data.noKk;
			niks = selectedRecord[0].data.nik;
		}
		catch(e){
			
		}
	});
	
	doLayoutWindow(kkGrid);
}

function callCetakDialog(){
	var form = new Ext.create('Ext.form.Panel', {
		bodyStyle:'padding:5px 5px 0',
		items: [{
            fieldLabel: 'Keperluan',
            id : 'jenisCetakComboBox',
            xtype : 'combobox',
            store:jenisCetakStore,
            displayField: 'name',
            valueField: 'id',
            editable:false
        }], 
        buttons: [
          {
        	  text: 'OK',
        	  handler:function(){
        		  jenisCetak = Ext.getCmp('jenisCetakComboBox').getValue();
        		  postCetakKartuKeluargaDetailOvr();
        		  postEditKartuKeluarga();
        	  }
          },{
        	  text: 'Cancel',
        	  handler:function(){
        		  formWin.close();
        	  }
          }
        ]
	});
	
	var formWin = Ext.create('Ext.window.Window', {
		id : 'jenisCetakPanel',
	    title: 'Jenis Cetak',
	    width:280,
	    height:110,
	    closeAction : 'destroy',
	    layout: 'fit',
	    items:[
	           form
        ]
	}).show();
}


function postSearchByNIK(target, nikRef){
	Ext.Ajax.request({
		url: '../PendudukController',
		params: {
			'command' : 'search',
			'nik' : nikRef
		},
		success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		target.setValue(obj.nama);
        		target.setReadOnly(false);
        	}
        	else{
        		target.setValue(obj.nama);
        		target.setReadOnly(true);
        	}
        },
        failure: function(response, opts){

        }
	});
}


function postAddKartuKeluarga(){
	Ext.Ajax.request({
		url: '../KartuKeluargaController',
        params: { 
            "command" : "add",
            "nik" : nik,
            "kelurahanID" : kelurahanID
        },
        success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		Ext.MessageBox.show({
  				  modal : true,
  		          title: 'Peringatan',
  		          msg: obj.explain,
  		          buttons: Ext.MessageBox.OK,
  		          icon: Ext.MessageBox.ERROR
  		      	});
        	}
        	else{
        		callDetailKKPanel('session');
        		Ext.MessageBox.show({
    				  modal : true,
    		          title: 'Peringatan',
    		          msg: obj.explain,
    		          buttons: Ext.MessageBox.OK,
    		          icon: Ext.MessageBox.INFO
		      	});
        	}
        },
        failure: function(response, opts){

        }
	});
}


function postEditKartuKeluarga(){
	Ext.Ajax.request({
		url: '../KartuKeluargaController',
        params: { 
            "command" : "edit",
            "jenisCetak" : jenisCetak,
            "nik" : niks,
            "noKK" : noKK
        },
        success: function(response, opts){
        	
        },
        failure: function(response, opts){

        }
	});
}

function postDeleteKartuKeluarga(){
	Ext.Ajax.request({
		url: '../KartuKeluargaController',
        params: { 
            "command" : "delete",
            "noKK" : noKK
        },
        success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		Ext.MessageBox.show({
  				  modal : true,
  		          title: 'Peringatan',
  		          msg: obj.explain,
  		          buttons: Ext.MessageBox.OK,
  		          icon: Ext.MessageBox.ERROR
  		      	});
        	}
        	else{
        		kkStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
	});
}

function postDeleteKartuKeluargaDetail(){
	Ext.Ajax.request({
		url: '../KartuKeluargaDetailController',
        params: { 
            "command" : "delete",
            "kkDetailID" : kkDetailID
        },
        success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		Ext.MessageBox.show({
  				  modal : true,
  		          title: 'Peringatan',
  		          msg: obj.explain,
  		          buttons: Ext.MessageBox.OK,
  		          icon: Ext.MessageBox.ERROR
  		      	});
        	}
        	else{
        		kkDetailAtasStore.load(function(records, operation, success) {
            	});
            	kkDetailBawahStore.load(function(records, operation, success) {
            	});
        	}
        },
        failure: function(response, opts){

        }
	});
}


function postCetakKartuKeluargaDetail(){
	Ext.Ajax.request({
		url: '../KartuKeluargaDetailController',
        params: { 
            "command" : "print",
            "type" : "new"
        },
        success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		Ext.MessageBox.show({
  				  modal : true,
  		          title: 'Peringatan',
  		          msg: 'Telah dilakukan proses pencetakan',
  		          buttons: Ext.MessageBox.OK,
  		          icon: Ext.MessageBox.ERROR
  		      	});
        	}
        	else{
        		Ext.MessageBox.show({
				  modal : true,
		          title: 'Peringatan',
		          msg: 'Telah dilakukan proses pencetakan',
		          buttons: Ext.MessageBox.OK,
		          icon: Ext.MessageBox.INFO
		      	});
        	}
        },
        failure: function(response, opts){

        }
	});
}

function postCetakKartuKeluargaDetailOvr(){
	Ext.Ajax.request({
		url: '../KartuKeluargaDetailController',
        params: { 
            "command" : "print",
            "type" : "repeat",
            "noKK" : noKK
        },
        success: function(response, opts){
        	var obj = Ext.JSON.decode(response.responseText);
        	if(obj.success == false){
        		Ext.MessageBox.show({
  				  modal : true,
  		          title: 'Peringatan',
  		          msg: 'Telah dilakukan proses pencetakan',
  		          buttons: Ext.MessageBox.OK,
  		          icon: Ext.MessageBox.ERROR
  		      	});
        	}
        	else{
        		Ext.MessageBox.show({
				  modal : true,
		          title: 'Peringatan',
		          msg: 'Telah dilakukan proses pencetakan',
		          buttons: Ext.MessageBox.OK,
		          icon: Ext.MessageBox.INFO
		      	});
        	}
        },
        failure: function(response, opts){

        }
	});
}