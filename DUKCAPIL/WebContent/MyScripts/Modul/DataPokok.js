var wilayahTree;
var agamaGrid;
var hubunganTree;
var pekerjaanGrid;
var pendidikanGrid;
var cacatGrid;

var wilayahStore;
var kecamatanStore;
var wilayahStore;
var agamaStore;
var hubunganStore;
var pekerjaanStore;
var pendidikanStore;
var cacatStore;

var wilayahRoot;
var hubunganRott;
var kecamatanID;
var kelurahanID;
var hubunganID;
var hubunganDetailID;
var pekerjaanID;
var pendidikanID;
var cacatID;

Ext.onReady(function(){
		
	var toolBar = toolBar = Ext.create('Ext.toolbar.Toolbar', {
		anchor:'100% 8%',
		items: [
		        {
		            xtype: 'button',
		            text : 'Wilayah',
		            handler : function(){
		            	callWilayahTree();
		            }
		        },
		        {
		        	xtype: 'button',
		        	text : 'Agama',
		        	handler : function(){
		        		callAgamaGrid();
		        	}
		        },
		        {
		        	xtype : 'button',
		        	text : 'Hubungan',
		        	handler : function(){
		        		callHubunganTree();
		        	}
		        },
		        {
		        	xtype : 'button',
		        	text : 'Pekerjaan',
		        	handler : function(){
		        		callPekerjaanGrid();
		        	}
		        },
		        {
		        	xtype : 'button',
		        	text : 'Pendidikan',
		        	handler : function(){
		        		callPendidikanGrid();
		        	}
		        },
		        {
		        	xtype: 'button',
		        	text : 'Cacat',
		        	handler : function(){
		        		callCacatGrid();
		        	}
		        }
		       ]
	});
	
	Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [{
	        region: 'north',
	        items:[toolBar]
	    }, {
	        region: 'center',
	        layout:'anchor',
	        id:'dataPokokPanel'
	    }]
	});

});

//LAYOUT
function doLayoutWindow(object){
	Ext.getCmp("dataPokokPanel").removeAll(true);
	Ext.getCmp("dataPokokPanel").add(object);
}

//REGION
function callWilayahTree(){
	wilayahStore = Ext.create('Ext.data.TreeStore', {
		proxy: {
			type: 'ajax',
			url: '../WilayahController?command=load'
		},
        root: {
            text: 'Salatiga',
            id: '0',
            iconCls : 'salatiga'
        },
		folderSort: true,
		sorters: [{
			property: 'text',
			direction: 'ASC'
		}]
	});
	
	wilayahTree = Ext.create('Ext.tree.Panel', {
		store: wilayahStore,
		anchor:'100% 100%',
		title: 'Struktur Wilayah',
		bbar:[
	          {
	        	  text : 'Detail',
	        	  iconCls : 'detail',
	        	  handler : function(){
	        		  if(wilayahRoot == "kelurahan"){
	        			  Ext.MessageBox.show({
	        				  modal : false,
	        		          title: 'Peringatan',
	        		          msg: 'Data yang ingin Anda lihat merupakan node wilayah dengan akses terkecil!',
	        		          buttons: Ext.MessageBox.OK,
	        		          icon: Ext.MessageBox.ERROR
	        		      });
	        		  }
	        		  else{
	        			  callWilayahWindow();
	        		  }
	        	  }
	          },
	          {
	        	  text : 'Hapus',
	        	  iconCls : 'delete',
	        	  handler : function(){
	        		  if(wilayahRoot == "kota"){
	        			  Ext.MessageBox.show({
	        				  modal : false,
	        		          title: 'Peringatan',
	        		          msg: 'Data yang ingin Anda hapus merupakan akar utama wilayah!',
	        		          buttons: Ext.MessageBox.OK,
	        		          icon: Ext.MessageBox.ERROR
	        		      });
	        		  }
	        		  else if(wilayahRoot == "kecamatan"){
	        			  postDeleteWilayah('kecamatan', kecamatanID);
	        		  }
	        		  else if(wilayahRoot == "kelurahan"){
	        			  postDeleteWilayah('kelurahan', kelurahanID);
	        		  }
	        	  }
	          }
        ]
	});
	
	wilayahTree.getSelectionModel().on('selectionchange', function(sm, selectedRecord){
		try{
			if(selectedRecord[0].data.iconCls == 'salatiga'){
				wilayahRoot = "kota";
			}
			else if(selectedRecord[0].data.iconCls == 'kecamatan'){
				wilayahRoot = "kecamatan";
				kecamatanID = selectedRecord[0].data.id.substring(1);
			}
			else if(selectedRecord[0].data.iconCls == 'kelurahan'){
				wilayahRoot = "kelurahan";
				kelurahanID = selectedRecord[0].data.id.substring(1);
			}
		}
		catch(err){
			
		}
	});
	
	doLayoutWindow(wilayahTree);
}

function callWilayahWindow(){
	var itemGrid = null;
	var newRow = false;
	var itemGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	if(wilayahRoot == 'kota'){
		Ext.define('Kecamatan',{
			extend : 'Ext.data.Model',
			fields : [
			{
				name : 'kecamatanId',
				type : 'string',
			},
			{
				name : 'nama',
				type : 'string',
			},
			{
				name : 'aktif',
				type : 'bool',
			},
			{
				name : 'kodeDaerah',
				type : 'string',
			},
	        ]
		});
		
		kecamatanStore = Ext.create('Ext.data.Store', {
			model : 'Kecamatan',
			proxy : {
				type: 'ajax',
		        url : '../WilayahController?command=load&select=kecamatan',
		        reader: {
		            type: 'json'
		        }
			},
		    autoLoad: true
		});
		
		itemGrid = Ext.create('Ext.grid.Panel', {
		    title: 'Data Kecamatan',
		    store: kecamatanStore,
		    anchor:'100% 100%',
		    columns: [
	            { header: 'ID Kecamatan',  dataIndex: 'kecamatanId', hideable:false, hidden:true},
		        { header: 'Kode Kecamatan',  dataIndex: 'kodeDaerah', 
            	  editor :{
            		  id:'KodeKecamatanTextField',
            		  allowBlank:false
            	}},
		        { header: 'Nama', dataIndex: 'nama',
		        	editor :{
            		  id:'NamaTextField',
            		  allowBlank:false
            	}},
		        { header: 'Aktif', dataIndex : 'aktif', xtype:'checkcolumn',
		        	editor :{
            		  id:'AktifCheckBox',
            		  xtype:'checkbox'
		        }}
		    ],
		    plugins: [itemGridEditing],
		    forceFit : true,
		    tbar:[
		          {
		        	  text : 'Tambah',
		        	  iconCls : 'add',
		        	  handler : function(){
		        		  itemGridEditing.cancelEdit();
		                  var r = Ext.create('Kecamatan', {
		                      kecamatanId: 0,
		                      nama: 'Nama Kecamatan',
		                      kodeDaerah: '00',
		                      aktif:false
		                  });
		                  kecamatanStore.insert(0, r);
		                  itemGridEditing.startEdit(0, 0);
		                  newRow = true;
		        	  }
		          }
	        ]
		});
		
		itemGridEditing.on('canceledit', function(o) {   
		    if(newRow == true)
		        kecamatanStore.removeAt(0);
		    newRow = false;
		});
		
		itemGridEditing.on('edit', function(o) {      
		    o.record.commit();
		    aktif = Ext.getCmp('AktifCheckBox').getValue();
		    if(newRow == true){
			    postAddWilayah('kecamatan', 0, o.record.get('nama'), o.record.get('kodeDaerah'));
			    newRow = false;
		    }
		    else{
		    	postEditWilayah("kecamatan", o.record.get("kecamatanId"), 0, o.record.get("nama"), o.record.get("kodeDaerah"));
		    }
		},this);
	}
	else if(wilayahRoot == 'kecamatan'){
		Ext.define('Kelurahan',{
			extend : 'Ext.data.Model',
			fields : [
			{
				name : 'kelurahanId',
				type : 'string',
			},
			{
				name : 'nama',
				type : 'string',
			},
			{
				name : 'aktif',
				type : 'bool',
			},
			{
				name : 'kodeDaerah',
				type : 'string',
			},
	        ]
		});
		
		kelurahanStore = Ext.create('Ext.data.Store', {
			model : 'Kelurahan',
			proxy : {
				type: 'ajax',
		        url : '../WilayahController?command=load&select=kelurahan&kecamatanID='+kecamatanID,
		        reader: {
		            type: 'json'
		        }
			},
		    autoLoad: true
		});
		
		itemGrid = Ext.create('Ext.grid.Panel', {
		    title: 'Data Kelurahan',
		    store: kelurahanStore,
		    anchor:'100% 100%',
		    columns: [
	            { header: 'ID Kelurahan',  dataIndex: 'kelurahanId', hideable:false, hidden:true},
		        { header: 'Kode Kelurahan',  dataIndex: 'kodeDaerah', 
            	  editor :{
            		  id:'KodeKelurahanTextField',
            		  allowBlank:false
            	}},
		        { header: 'Nama', dataIndex: 'nama',
		        	editor :{
            		  id:'NamaTextField',
            		  allowBlank:false
            	}},
		        { header: 'Aktif', dataIndex : 'aktif', xtype:'checkcolumn',
		        	editor :{
            		  id:'AktifCheckBox',
            		  xtype:'checkbox'
		        }}
		    ],
		    plugins: [itemGridEditing],
		    forceFit : true,
		    tbar:[
		          {
		        	  text : 'Tambah',
		        	  iconCls : 'add',
		        	  handler : function(){
		        		  itemGridEditing.cancelEdit();
		                  var r = Ext.create('Kelurahan', {
		                      kelurahanId: 0,
		                      nama: 'Nama Kelurahan',
		                      kodeDaerah: '00',
		                      aktif:false
		                  });
		                  kelurahanStore.insert(0, r);
		                  itemGridEditing.startEdit(0, 0);
		                  newRow = true;
		        	  }
		          }
	        ]
		});
		
		itemGridEditing.on('canceledit', function(o) {   
		    if(newRow == true)
		        kelurahanStore.removeAt(0);
		    newRow = false;
		});
		
		itemGridEditing.on('edit', function(o) {      
		    o.record.commit();
		    aktif = Ext.getCmp('AktifCheckBox').getValue();
		    if(newRow == true){
			    postAddWilayah('kelurahan', kecamatanID, o.record.get('nama'), o.record.get('kodeDaerah'));
			    newRow = false;
		    }
		    else{
		    	postEditWilayah('kelurahan',  kecamatanID,o.record.get('kelurahanId'), o.record.get('nama'), o.record.get('kodeDaerah'));
		    }
		},this);
	}
	
	var panelDetail = Ext.widget('window', {
        title: 'Manajemen Wilayah',
        closeAction: 'destroy',
        width: 500,
        height: 350,
        layout:'anchor',
        items:[
               itemGrid
              ],
        modal:false
    });
	panelDetail.setPosition(50,50,true);
	panelDetail.show();
}

//RELIGON
function callAgamaGrid(){
	var newRow = false;
	var agamaGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
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
	        url : '../AgamaController?command=load',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	agamaGrid = Ext.create('Ext.grid.Panel', {
	    title: 'Data Agama',
	    store: agamaStore,
	    anchor:'100% 100%',
	    loadMask: true,
	    columns: [
	        { header: 'ID Agama',  dataIndex: 'agamaId', hidden:true, hideable:false },
	        { header: 'Nama', dataIndex: 'nama',
	        	editor:{
	        		id:'NamaTextField',
        	}}
	    ],
	    forceFit : true,
	    plugins:[agamaGridEditing],
	    bbar :Ext.create('Ext.PagingToolbar', {
	            store: agamaStore,
	            displayInfo: true,
	            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
	            emptyMsg: "No topics to display",
	            items:[
					{
						  text : 'Tambah',
						  iconCls : 'add',
						  handler : function(){
							  agamaGridEditing.cancelEdit();
					          var r = Ext.create('Agama', {
					        	  agamaId: 0,
					        	  nama: 'Nama Agama'
					          });
					          agamaStore.insert(0, r);
					          agamaGridEditing.startEdit(0, 0);
					          newRow = true;
						  }
					},
					{
						  text : 'Hapus',
						  iconCls : 'delete',
						  handler: function(){
							  postDeleteAgama(agamaID);
						  }
					}
                ]
          })
	});
	
	agamaGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			agamaID = selectedRecord[0].data.agamaId;
		}
		catch(err){
			
		}
	});
	
	agamaGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        agamaStore.removeAt(0);
	    newRow = false;
	});
	
	agamaGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    if(newRow == true){
		    postAddAgama(o.record.get('nama'));
		    newRow = false;
	    }
	    else{
	    	postEditAgama(o.record.get('nama'), o.record.get('agamaId'));
	    }
	},this);
	
	doLayoutWindow(agamaGrid);
}

//RELATION
function callHubunganTree(){
	hubunganStore = Ext.create('Ext.data.TreeStore', {
		proxy: {
			type: 'ajax',
			url: '../HubunganController?command=load'
		},
        root: {
            text: 'Hubungan',
            id: '0',
            iconCls : 'relation'
        },
		folderSort: true,
		sorters: [{
			property: 'text',
			direction: 'ASC'
		}]
	});
	
	hubunganTree = Ext.create('Ext.tree.Panel', {
		store: hubunganStore,
		anchor:'100% 100%',
		title: 'Struktur Hubungan',
		bbar:[
	          {
	        	  text : 'Detail',
	        	  iconCls : 'detail',
	        	  handler : function(){
	        		  if(hubunganRoot == "file"){
	        			  Ext.MessageBox.show({
	        				  modal : false,
	        		          title: 'Peringatan',
	        		          msg: 'Data yang ingin Anda lihat merupakan node hubungan dengan akses terkecil!',
	        		          buttons: Ext.MessageBox.OK,
	        		          icon: Ext.MessageBox.ERROR
	        		      });
	        		  }
	        		  else{
	        			  dataHubunganWindow();
	        		  }
	        	  }
	          },
	          {
	        	  text : 'Hapus',
	        	  iconCls : 'delete',
	        	  handler : function(){
	        		  if(hubunganRoot == "relation"){
	        			  Ext.MessageBox.show({
	        				  modal : false,
	        		          title: 'Peringatan',
	        		          msg: 'Data yang ingin Anda hapus merupakan akar hubungan utama!',
	        		          buttons: Ext.MessageBox.OK,
	        		          icon: Ext.MessageBox.ERROR
	        		      });
	        		  }
	        		  else if(hubunganRoot == "folder"){
	        			  postDeleteHubungan("hubungan", hubunganID);
	        		  }
	        		  else if(hubunganRoot == "file"){
	        			  postDeleteHubungan("hubunganDetail",hubunganDetailID);
	        		  }
	        	  }
	          }
        ]
	});
	
	hubunganTree.getSelectionModel().on('selectionchange', function(sm, selectedRecord){
		try{
			if(selectedRecord[0].data.iconCls == 'relation'){
				hubunganRoot = "relation";
			}
			else if(selectedRecord[0].data.iconCls == 'folder'){
				hubunganRoot = 'folder';
				hubunganID = selectedRecord[0].data.id.substring(1);
			}
			else if(selectedRecord[0].data.iconCls == 'file'){
				hubunganRoot = 'file';
				hubunganDetailID = selectedRecord[0].data.id.substring(1);
			}
		}
		catch(err){
			
		}
	});
	
	doLayoutWindow(hubunganTree);
}

function dataHubunganWindow(){
	var itemGrid = null;
	var newRow = false;
	var itemGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	if(hubunganRoot == "relation"){
		Ext.define('Hubungan',{
			extend : 'Ext.data.Model',
			fields : [
			{
				name : 'hubunganId',
				type : 'string',
			},
			{
				name : 'nama',
				type : 'string',
			}
	        ]
		});
		
		hubunganGridStore = Ext.create('Ext.data.Store', {
			model : 'Hubungan',
			proxy : {
				type: 'ajax',
		        url : '../HubunganController?command=load&select=hubungan',
		        reader: {
		            type: 'json'
		        }
			},
		    autoLoad: true
		});
		
		itemGrid = Ext.create('Ext.grid.Panel', {
		    title: 'Data Hubungan',
		    store: hubunganGridStore,
		    anchor:'100% 100%',
		    columns: [
		        { header: 'ID Hubungan',  dataIndex: 'hubunganId', hidden:true, hideable:false },
		        { header: 'Nama', dataIndex: 'nama',
		        	editor:{
		        		id:'NamaTextField',
	        	}}
		    ],
		    forceFit : true,
		    plugins:[itemGridEditing],
		    tbar:[
		          {
		        	  text : 'Tambah',
		        	  iconCls : 'add',
		        	  handler : function(){
		        		  itemGridEditing.cancelEdit();
		                  var r = Ext.create('Hubungan', {
		                      hubunganId: 0,
		                      nama: 'Nama Hubungan'
		                  });
		                  hubunganGridStore.insert(0, r);
		                  itemGridEditing.startEdit(0, 0);
		                  newRow = true;
		        	  }
		          }
	        ]
		});
		
		itemGridEditing.on('canceledit', function(o) {   
		    if(newRow == true)
		    	hubunganGridStore.removeAt(0);
		    newRow = false;
		});
		
		itemGridEditing.on('edit', function(o) {      
		    o.record.commit();
		    if(newRow == true){
			    postAddHubungan("hubungan", 0, o.record.get("nama"));
			    newRow = false;
		    }
		    else{
		    	hubunganID = o.record.get("hubunganId");
		    	postEditHubungan("hubungan", o.record.get("nama"),hubunganID, 0);
		    }
		},this);
	}
	else if(hubunganRoot == "folder"){
		Ext.define('HubunganDetail',{
			extend : 'Ext.data.Model',
			fields : [
			{
				name : 'hubunganDetailId',
				type : 'string',
			},
			{
				name : 'nama',
				type : 'string',
			}
	        ]
		});
		
		hubunganDetailStore = Ext.create('Ext.data.Store', {
			model : 'HubunganDetail',
			proxy : {
				type: 'ajax',
		        url : '../HubunganController?command=load&select=hubunganDetail&hubunganID=' + hubunganID,
		        reader: {
		            type: 'json'
		        }
			},
		    autoLoad: true
		});
		
		itemGrid = Ext.create('Ext.grid.Panel', {
		    title: 'Data Hubungan',
		    store: hubunganDetailStore,
		    anchor:'100% 100%',
		    collapsible : true,
		    columns: [
		        { header: 'ID Hubungan',  dataIndex: 'hubunganDetailId', hidden:true, hideable:false },
		        { header: 'Nama', dataIndex: 'nama',
		        	editor:{
		        		id:'NamaTextField',
	        	}}
		    ],
		    forceFit : true,
		    plugins:[itemGridEditing],
		    tbar:[
		          {
		        	  text : 'Tambah',
		        	  iconCls : 'add',
		        	  handler : function(){
		        		  itemGridEditing.cancelEdit();
		                  var r = Ext.create('HubunganDetail', {
		                      hubunganId: 0,
		                      nama: 'Nama Hubungan'
		                  });
		                  hubunganDetailStore.insert(0, r);
		                  itemGridEditing.startEdit(0, 0);
		                  newRow = true;
		        	  }
		          }
	        ]
		});
		
		itemGridEditing.on('canceledit', function(o) {   
		    if(newRow == true)
		        hubunganStore.removeAt(0);
		    newRow = false;
		});
		
		itemGridEditing.on('edit', function(o) {      
		    o.record.commit();
		    if(newRow == true){
			    postAddHubungan("hubunganDetail", hubunganID, o.record.get("nama"));
			    newRow = false;
		    }
		    else{
		    	hubunganDetailID = o.record.get("hubunganDetailId");
		    	postEditHubungan("hubunganDetail",o.record.get("nama"), hubunganID, hubunganDetailID);
		    }
		},this);
		
	}
	var panelDetail = Ext.widget('window', {
        title: 'Manajemen Hubungan',
        closeAction: 'destroy',
        width: 500,
        height: 350,
        items:[
               itemGrid
              ],
        layout:'anchor',
        modal:false
    });
	panelDetail.setPosition(50,50,true);
	panelDetail.show();
}

//JOB
function callPekerjaanGrid(){
	var newRow = false;
	var pekerjaanGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
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
	    pageSize : 12,
	    proxy: {
	        type: 'ajax',
	        url : '../PekerjaanController?command=load',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	pekerjaanGrid = Ext.create('Ext.grid.Panel', {
	    title: 'Data Pekerjaan',
	    store: pekerjaanStore,
	    anchor:'100% 100%',
	    columns: [
	        { header: 'ID Pekerjaan',  dataIndex: 'pekerjaanId', hidden:true, hideable:false },
	        { header: 'Nama', dataIndex: 'nama',
	        	editor:{
	        		id:'NamaTextField',
        	}}
	    ],
	    forceFit : true,
	    plugins:[pekerjaanGridEditing],
	    bbar :Ext.create('Ext.PagingToolbar', {
            store: pekerjaanStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display",
            items:[
					{
						  text : 'Tambah',
						  iconCls : 'add',
						  handler : function(){
							  pekerjaanGridEditing.cancelEdit();
					        var r = Ext.create('Pekerjaan', {
					            pekerjaanId: 0,
					            nama: 'Nama Pekerjaan'
					        });
					        pekerjaanStore.insert(0, r);
					        pekerjaanGridEditing.startEdit(0, 0);
					        newRow = true;
						  }
					},
					{
						  text : 'Hapus',
						  iconCls : 'delete',
						  handler: function(){
							  postDeletePekerjaan();
						  }
					}
                  ]
	    })
	});
	
	pekerjaanGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	    	pekerjaanStore.removeAt(0);
	    newRow = false;
	});
	
	pekerjaanGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    if(newRow == true){
	    	postAddPekerjaan(o.record.get("nama"));
		    newRow = false;
	    }
	    else{
	    	pekerjaanID = o.record.get('pekerjaanId');
	    	postEditPekerjaan(o.record.get('nama'));
	    }
	},this);
	
	pekerjaanGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			pekerjaanID = selectedRecord[0].data.pekerjaanId;
		}
		catch(err){
			
		}
	});
	
	doLayoutWindow(pekerjaanGrid);
}

//EDUCATION
function callPendidikanGrid(){
	var newRow = false;
	var pendidikanGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
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
	    pageSize : 12,
	    proxy: {
	        type: 'ajax',
	        url : '../PendidikanController?command=load',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	pendidikanGrid = Ext.create('Ext.grid.Panel', {
	    title: 'Data Pendidikan',
	    store: pendidikanStore,
	    anchor:'100% 100%',
	    columns: [
	        { header: 'ID Pendidikan',  dataIndex: 'pekerjaanId', hidden:true, hideable:false },
	        { header: 'Nama', dataIndex: 'nama',
	        	editor:{
	        		id:'NamaTextField',
	        		allowBlank:false
        	}}
	    ],
	    forceFit : true,
	    plugins:[pendidikanGridEditing],
	    bbar :Ext.create('Ext.PagingToolbar', {
            store: pendidikanStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display",
            items:[
			          {
			        	  text : 'Tambah',
			        	  iconCls : 'add',
			        	  handler : function(){
			        		  pendidikanGridEditing.cancelEdit();
			                  var r = Ext.create('Pendidikan', {
			                      pekerjaanId: 0,
			                      nama: 'Nama Pendidikan'
			                  });
			                  pendidikanStore.insert(0, r);
			                  pendidikanGridEditing.startEdit(0, 0);
			                  newRow = true;
			        	  }
			          },
			          {
			        	  text : 'Hapus',
			        	  iconCls : 'delete',
			        	  handler: function(){
			        		  postDeletePendidikan();
			        	  }
			          }
	       ]
	    })
	});
	
	pendidikanGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	    	pendidikanStore.removeAt(0);
	    newRow = false;
	});
	
	pendidikanGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    if(newRow == true){
	    	postAddPendidikan(o.record.get("nama"));
		    newRow = false;
	    }
	    else{
	    	pendidikanID = o.record.get('pendidikanId');
	    	postEditPendidikan(o.record.get('nama'));
	    }
	},this);
	
	pendidikanGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			pendidikanID = selectedRecord[0].data.pendidikanId;
		}
		catch(err){
			
		}
	});
	
	doLayoutWindow(pendidikanGrid);
}

//CACAT
function callCacatGrid(){
	var newRow = false;
	var cacatGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
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
	        url : '../CacatController?command=load',
	        reader: {
	            type: 'json'
	        }
	    },
	    autoLoad: true
	});
	
	cacatGrid = Ext.create('Ext.grid.Panel', {
	    title: 'Data Cacat',
	    store: cacatStore,
	    anchor:'100% 100%',
	    collapsible : true,
	    loadMask: true,
	    columns: [
	        { header: 'ID Cacat',  dataIndex: 'cacatId', hidden:true, hideable:false },
	        { header: 'Nama', dataIndex: 'nama',
	        	editor:{
	        		id:'NamaTextField',
        	}}
	    ],
	    forceFit : true,
	    plugins:[cacatGridEditing],
	    bbar :Ext.create('Ext.PagingToolbar', {
	            store: cacatStore,
	            displayInfo: true,
	            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
	            emptyMsg: "No topics to display",
	            items:[
					{
						  text : 'Tambah',
						  iconCls : 'add',
						  handler : function(){
							  cacatGridEditing.cancelEdit();
					          var r = Ext.create('Cacat', {
					        	  cacatId: 0,
					        	  nama: 'Nama Cacat'
					          });
					          cacatStore.insert(0, r);
					          cacatGridEditing.startEdit(0, 0);
					          newRow = true;
						  }
					},
					{
						  text : 'Hapus',
						  iconCls : 'delete',
						  handler: function(){
							  postDeleteCacat(cacatID);
						  }
					}
                ]
          })
	});
	
	cacatGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			cacatID = selectedRecord[0].data.cacatId;
		}
		catch(err){
			
		}
	});
	
	cacatGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        cacatStore.removeAt(0);
	    newRow = false;
	});
	
	cacatGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    if(newRow == true){
		    postAddCacat(o.record.get('nama'));
		    newRow = false;
	    }
	    else{
	    	cacatID = o.record.get('cacatId');
	    	postEditCacat(o.record.get('nama'));
	    }
	},this);
	
	doLayoutWindow(cacatGrid);
}


//POST TO SERVER
function postAddWilayah(scope,kecamatanID,nama,kodeDaerah){
	Ext.Ajax.request({
        url: "../WilayahController",
        params: { 
            "command" : "add",
            "scope" : scope,
            "kecamatanID" : kecamatanID,
            "nama" : nama,
            "kodeDaerah" : kodeDaerah,
            "aktif" : aktif
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
        		wilayahTree.getStore().load();
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditWilayah(scope,kecamatanID, kelurahanID,nama,kodeDaerah){
	Ext.Ajax.request({
        url: "../WilayahController",
        params: { 
            "command" : "edit",
            "scope" : scope,
            "kelurahanID" : kelurahanID,
            "kecamatanID" : kecamatanID,
            "nama" : nama,
            "kodeDaerah" : kodeDaerah,
            "aktif" : aktif
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
        		wilayahTree.getStore().load();
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteWilayah(scope, ID){
	Ext.Ajax.request({
        url: "../WilayahController",
        params: { 
            "command" : "delete",
            "scope" : scope,
            "ID" : ID
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
        		wilayahTree.getStore().load();
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddAgama(nama){
	Ext.Ajax.request({
        url: '../AgamaController',
        params: { 
            "command" : "add",
            "nama" : nama
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
        		agamaStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditAgama(nama, agamaID){
	Ext.Ajax.request({
        url: '../AgamaController',
        params: { 
            "command" : "edit",
            "nama" : nama,
            "agamaID" : agamaID
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
        		agamaStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteAgama(agamaID){
	Ext.Ajax.request({
        url: '../AgamaController',
        params: { 
            "command" : "delete",
            "agamaID" : agamaID
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
        		agamaStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddHubungan(scope, hubunganID, nama){
	Ext.Ajax.request({
        url: '../HubunganController',
        params: { 
            "command" : "add",
            "hubunganID" : hubunganID,
            "nama" : nama,
            "scope" : scope
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
        		hubunganTree.getStore().load();
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditHubungan(scope, nama, hubunganID, hubunganDetailID){
	Ext.Ajax.request({
        url: '../HubunganController',
        params: { 
            "command" : "edit",
            "hubunganID" : hubunganID,
            "hubunganDetailID" : hubunganDetailID,
            "nama" : nama,
            "scope" : scope
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
        		hubunganTree.getStore().load();
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteHubungan(scope, ID){
	Ext.Ajax.request({
        url: '../HubunganController',
        params: { 
            "command" : "delete",
            "ID" : ID,
            "scope" : scope
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
        		hubunganTree.getStore().load();
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddPekerjaan(nama){
	Ext.Ajax.request({
        url: '../PekerjaanController',
        params: { 
            "command" : "add",
            "nama" : nama
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
        		pekerjaanStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditPekerjaan(nama){
	Ext.Ajax.request({
        url: '../PekerjaanController',
        params: { 
            "command" : "edit",
            "pekerjaanID" : pekerjaanID,
            "nama" : nama
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
        		pekerjaanStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeletePekerjaan(){
	Ext.Ajax.request({
        url: '../PekerjaanController',
        params: { 
            "command" : "delete",
            "pekerjaanID" : pekerjaanID
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
        		pekerjaanStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddPendidikan(nama){
	Ext.Ajax.request({
        url: '../PendidikanController',
        params: { 
            "command" : "add",
            "nama" : nama
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
        		pendidikanStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditPendidikan(nama){
	Ext.Ajax.request({
        url: '../PendidikanController',
        params: { 
            "command" : "edit",
            "pendidikanID" : pendidikanID,
            "nama" : nama
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
        		pendidikanStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeletePendidikan(){
	Ext.Ajax.request({
        url: '../PendidikanController',
        params: { 
            "command" : "delete",
            "pendidikanID" : pendidikanID
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
        		pendidikanStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddCacat(nama){
	Ext.Ajax.request({
        url: '../CacatController',
        params: { 
            "command" : "add",
            "nama" : nama
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
        		cacatStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditCacat(nama){
	Ext.Ajax.request({
        url: '../CacatController',
        params: { 
            "command" : "edit",
            "nama" : nama,
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
        		cacatStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteCacat(){
	Ext.Ajax.request({
        url: '../CacatController',
        params: { 
            "command" : "delete",
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
        		cacatStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}