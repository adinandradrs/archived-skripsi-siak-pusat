var ktpGrid;
var bioGrid;
var detailTemplate;

var ktpStore;
var bioStore;

var roleCetak;
var tanggalLahir;
var expired;
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
	
	ktpMenu = Ext.create('Ext.panel.Panel', {
	    title: 'Menu Utama',
	    anchor:'100%',
	    tbar: [{
	    	xtype: 'buttongroup',
	        columns: 3,
	        title: 'Pilihan',
	        collapsible:true,
	        items: [
				{
	        		text: 'Detail',
	        		scale: 'large',
	        		iconCls: 'detaildk',
	                iconAlign: 'top',
	                handler:function(){
	        			detailTemplate.overwrite(Ext.getCmp('detailPortraitPanel').body, data);
	                }
	        	},
	        	{
					text: 'Lihat',
					scale: 'large',
					iconCls: 'tabeldk',
				    iconAlign: 'top',
				    handler:function(){
				    	callPendudukWindow();
				    }
				},                
				{
                	text: 'Cetak',
                	scale: 'large',
                	iconCls: 'cetakdk',
                    iconAlign: 'top',
                    handler:function(){
                    	if(roleCetak === "new"){
                    		postAddKTP();
                    	}
                    	else if(roleCetak === "edit"){
                    		callCetakDialog();
                    	}
                    }
	        	}
	        ]
	    }]
	});
	
	Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [
	        {
	        	region: 'north',
	        	items:[
	        	       ktpMenu
	        	]
	        },
	        {
		        region: 'center',
		        layout:'border',
		        items:[
		               {
		            	   title:'KTP Penduduk',
		            	   xtype:'panel',
		            	   layout:'anchor',
		            	   region:'center',
		            	   items :[
		            	           callKTPGrid()
		            	   ]
		               },
		               {
		            	   id:'detailPortraitPanel',
		            	   title:'Portrait',
		            	   xtype:'panel',
		            	   region:'east',
		            	   html:'',
		            	   width:280
		               }
	            ]
		    }
        ]
	});
	
});

function renderExpired(val) {
	var today = new Date();
	var compare = new Date(val);
    if (compare > today) {
        return '<span style="color:green;">' + val + '</span>';
    } else if (compare < today) {
        return '<span style="color:red;">' + val + '</span>';
    }
    return val;
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
        		  postEditKTP();
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
			name : 'tempatLahir',
			type : 'string'
		},
		{
			name : 'tanggalLahir',
			type : 'string'
		},
		{
			name : 'jenisKelamin',
			type : 'string'
		},
		{
			name : 'alamat',
			type : 'string'
		},
		{
			name : 'kelurahan',
			type : 'string'
		},
		{
			name : 'kecamatan',
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
			name : 'expired',
			type : 'string'
		},
		{
			name : 'status',
			type : 'string'
		},
		{
			name : 'rt',
			type : 'string'
		},
		{
			name : 'rw',
			type : 'string'
		},
		{
			name : 'foto',
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
		store: ktpStore,
	    anchor:'100% 100%',
	    columns: [
            {header:'NIK', dataIndex:'nik', id:'nik',filter: {
	            type: 'string'   
            }},
            {header:'Nama', dataIndex:'nama'},
            {header:'Jenis Buat', dataIndex:'status'},
            {header:'Expired', dataIndex:'expired',renderer : renderExpired,}
	    ],
	    forceFit : true,
	    features: [filters],
	    tbar : Ext.create('Ext.PagingToolbar', {
            store: ktpStore,
            displayInfo: true,
            displayMsg: 'Data KTP {0} - {1} dari {2}',
            emptyMsg: "Tidak terdapat data"
        })
	});
	
	var detailFormat = [
	                     'NIK : {nik}<br/>',
	                     'Nama: {nama}<br/>',
	                     'Tempat/Tgl.Lahir: {tempatLahir}, {tanggalLahir}<br/>',
	                     'Jenis Kelamin: {jenisKelamin}<br/>',
	                     'Alamat: {alamat}<br/>',
	                     'RT/RW: {rt}/{rw}<br/>',
	                     'Desa/Kel: {kelurahan}<br/>',
	                     'Kecamatan: {kecamatan}<br/>',
	                     'Kota: SALATIGA<br/>',
	                     'Agama: {agama}<br/>',
	                     'Pekerjaan: {pekerjaan}<br/>',
	                     'Expired: {expired}<br/>',
	                     '{foto}<br/>'
	                   ];
	detailTemplate = Ext.create('Ext.Template', detailFormat);
	
	ktpGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			roleCetak = "edit";
			data = selectedRecord[0].data;
			nik = selectedRecord[0].data.nik;
			expired = selectedRecord[0].data.expired;
		}
		catch(e){
			
		}
	});
	
	return ktpGrid;
}

function callPendudukWindow(){
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
			roleCetak = "new";
			tanggalLahir = selectedRecord[0].data.tanggalLahir;
			nik = selectedRecord[0].data.nik;
		}
		catch(e){
			
		}
	});
	
	var pendudukWin = Ext.create('Ext.window.Window', {
	    title: 'Data Penduduk',
	    width:510,
	    height:300,
	    closeAction : 'destroy',
	    layout: 'fit',
	    items:[
	           bioGrid
        ]
	}).show();
}

function postAddKTP(){
	Ext.Ajax.request({
        url: '../KtpController',
        params: { 
            "command" : "add",
            "nik" : nik,
            "tanggalLahir" : tanggalLahir
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
        		ktpStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditKTP(){
	Ext.Ajax.request({
        url: '../KtpController',
        params: { 
            "command" : "edit",
            "jenisCetak" : jenisCetak,
            "nik" : nik,
            "expired" : expired
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
        		ktpStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}