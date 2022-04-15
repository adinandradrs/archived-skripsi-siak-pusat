//GLOBAL VARIABEL
var rolePanel;

var penggunaGrid;
var grupGrid;
var grupTree;
var roleKecamatanGrid;
var roleDUKCAPILGrid;
var roleKelurahanGrid;
var kotaGrid;

var penggunaStore;
var grupStore;
var grupTreeStore;
var roleKecamatanStore;
var roleDUKCAPILStore;
var roleKelurahanStore;
var availableUserStore;
var kotaStore;

var username;
var grupID;
var kelurahanID;
var kecamatanID;
var grupPenggunaID;
var kotaID;

Ext.onReady(function(){
	var penggunaMenu = Ext.create('Ext.menu.Menu', {
	    items: [{
	        text: 'Pengguna',
	        handler:function(){
	        	callPenggunaGrid();
	        }
	    },{
	        text: 'Grup',
	        handler:function(){
	        	callGrupGrid();
	        }
	    },{
	        text: 'Role Pengguna',
	        handler:function(){
	        	callRolePanel();
	        }
	    },{
	        text: 'DUKCAPIL Lain',
	        handler:function(){
	        	callKotaGrid();
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
	        items:[penggunaMenu]
	    }, {
	        region: 'center',
	        layout:'anchor',
	        id:'penggunaPanel'
	    }]
	});
	
});

//LAYOUT
function doLayoutWindow(object){
	Ext.getCmp('penggunaPanel').removeAll(true);
	Ext.getCmp('penggunaPanel').add(object);
}

//USER
function callPenggunaGrid(){
	var newRow = false;
	var penggunaGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	Ext.define('Pengguna',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'username',
			type : 'string'
		},
		{
			name : 'nama',
			type : 'string',
		},
		{
			name : 'sandi',
			type : 'string'
		},
		{
			name : 'ipLogin',
			type : 'string'
		},
		{
			name : 'waktuLogin',
			type : 'string'
		},
		{
			name : 'aktif',
			type : 'bool'
		},
        ]
	});
	
	penggunaStore = Ext.create('Ext.data.Store', {
		model : 'Pengguna',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../PenggunaController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	penggunaGrid = Ext.create('Ext.grid.Panel', {
	    store: penggunaStore,
	    anchor:'100% 100%',
	    title : 'Data Pengguna',
	    columns: [
            { header: 'Username',  dataIndex: 'username',editor:{
            	id:'usernameTextField',
            	allowBlank :false
            }},
	        { header: 'Nama',  dataIndex: 'nama',editor:{
            	id:'namaTextField',
            	allowBlank :false
            }},
	        { header: 'IP Login', dataIndex: 'ipLogin'},
	        { header: 'Waktu Login', dataIndex: 'waktuLogin'},
	        { header: 'Aktif', dataIndex: 'aktif', xtype:'checkcolumn',editor:{
            	id:'aktifCheckBox',
            	xtype:'checkbox'
            }}
	    ],
	    forceFit : true,
	    plugins:[penggunaGridEditing],
	    bbar : Ext.create('Ext.PagingToolbar', {
            store: penggunaStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display",
            items:[
					{
						  text : 'Tambah',
						  iconCls : 'add',
						  handler : function(){
							  penggunaGridEditing.cancelEdit();
							  var r = Ext.create('Pengguna', {
					        	  username: 'Username pengguna',
					        	  nama: 'Nama pengguna',
					        	  aktif:false
					          });
					          penggunaStore.insert(0, r);
					          penggunaGridEditing.startEdit(0, 0);
					          newRow = true;
						  }
					},
					{
						  text : 'Hapus',
						  iconCls : 'delete',
						  handler: function(){
							  postDeletePengguna(username);
						  }
					}
            ]
	    })
	});
	
	penggunaGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        penggunaStore.removeAt(0);
	    newRow = false;
	});
	
	penggunaGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    username = o.record.get("username");
	    var nama = o.record.get("nama");
	    var aktif = Ext.getCmp("aktifCheckBox").getValue();
	    var sandi = o.record.get("sandi");
	    var waktuLogin = o.record.get("waktuLogin");
	    var ipLogin = o.record.get("ipLogin");
	    if(newRow == true){
		    postAddPengguna(username, nama, aktif);
		    newRow = false;
	    }
	    else{
	    	postEditPengguna(username, nama, sandi, waktuLogin, ipLogin, aktif);
	    }
	},this);
	
	penggunaGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			username = selectedRecord[0].data.username;
		}
		catch(err){
			
		}
	});
	
	doLayoutWindow(penggunaGrid);
}

//GROUP
function callGrupGrid(){
	var newRow = false;
	var grupGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	Ext.define('Grup',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'grupId',
			type : 'string',
		},
		{
			name : 'nama',
			type : 'string',
		},
		{
			name : 'aktif',
			type : 'bool'
		}
        ]
	});
	
	grupStore = Ext.create('Ext.data.Store', {
		model : 'Grup',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../GrupController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	grupGrid = Ext.create('Ext.grid.Panel', {
	    store: grupStore,
	    anchor:'100% 100%',
	    columns: [
            { header: 'Grup ID',  dataIndex: 'grupId', hideable:false, hidden:true},
	        { header: 'Nama',  dataIndex: 'nama', editor:{
	        	id : 'namaTextField',
	        	allowBlank : false
	        }},
	        { header: 'Aktif',  dataIndex: 'aktif', xtype:'checkcolumn', editor:{
	        	id : 'aktifCheckBox',
	        	xtype : 'checkbox'
	        }}
	    ],
	    forceFit : true,
	    plugins:[grupGridEditing],
	    bbar : Ext.create('Ext.PagingToolbar', {
            store: grupStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display",
            items:[
					{
						  text : 'Tambah',
						  iconCls : 'add',
						  handler : function(){
							  grupGridEditing.cancelEdit();
							  var r = Ext.create('Grup', {
					        	  nama: 'Nama Grup',
					        	  aktif:false
					          });
					          grupStore.insert(0, r);
					          grupGridEditing.startEdit(0, 0);
					          newRow = true;
						  }
					},
					{
						  text : 'Hapus',
						  iconCls : 'delete',
						  handler: function(){
							  postDeleteGrup(grupID);
						  }
					}
            ]
	    })
	});
	
	grupGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        grupStore.removeAt(0);
	    newRow = false;
	});
	
	grupGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    var nama = o.record.get("nama");
	    grupID = o.record.get("grupId");
	    var aktif = Ext.getCmp("aktifCheckBox").getValue();
	    if(newRow == true){
		    postAddGrup(nama,aktif);
		    newRow = false;
	    }
	    else{
	    	postEditGrup(grupID, nama, aktif);
	    }
	},this);
	
	grupGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			grupID = selectedRecord[0].data.grupId;
		}
		catch(err){
			
		}
	});
	
	doLayoutWindow(grupGrid);
}

function initAvailableUser(){
	Ext.define('AvailableUser',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'username',
			type : 'string',
		},
        ]
	});
	
	availableUserStore = Ext.create('Ext.data.Store', {
		model : 'AvailableUser',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../GrupPenggunaController?command=load&type=combobox',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	return availableUserStore;
}

//ROLE
function doLayoutRolePanel(object){
	Ext.getCmp("rolePanel").removeAll(true);
	Ext.getCmp("rolePanel").add(object);
}

function callRolePanel(){
	grupTreeStore = Ext.create('Ext.data.TreeStore', {
		proxy: {
			type: 'ajax',
			url: '../GrupPenggunaController?command=load&type=tree'
		},
        root: {
            text: 'Grup Pengguna',
            id: '0',
            iconCls : 'user-group'
        },
		folderSort: true,
		sorters: [{
			property: 'text',
			direction: 'ASC'
		}]
	});
	
	grupTree = Ext.create('Ext.tree.Panel', {
		store: grupTreeStore,
		anchor:'100% 100%',
		singleExpand : true
	});
	
	grupTree.on("itemexpand", function(node){
		grupID = node.data.id.substring(1);
	});
	
	rolePanel = Ext.create('Ext.panel.Panel',{
		anchor:'100% 100%',
	    layout: 'border',
	    items: [{
	        title: 'Akses',
	        region:'west',
	        xtype:'panel',
	        layout:'anchor',
	        width: 180,
	        collapsible:true,
	        items:[
	               grupTree
	              ]
	    },{
	        title: 'Pengguna',
	        region: 'center',
	        id:'rolePanel',
	        layout:'anchor'
	    }]
	});
	
	grupTree.getSelectionModel().on('selectionchange', function(sm, selectedRecord){
		try{
			if(selectedRecord[0].data.iconCls == 'kecamatan'){
				kecamatanID = selectedRecord[0].data.id.substring(2);
				doLayoutRolePanel(callRoleKecamatanGrid(grupID));
			}
			else if(selectedRecord[0].data.iconCls == 'kelurahan'){
				kelurahanID = selectedRecord[0].data.id.substring(2);
				doLayoutRolePanel(callRoleKelurahanGrid(grupID));
			}
			else if(selectedRecord[0].data.text == 'DUKCAPIL'){	
				grupID = selectedRecord[0].data.id.substring(1);
				doLayoutRolePanel(callRoleDUKCAPILGrid(grupID));
			}
		}
		catch(err){
		}
	});
	
	doLayoutWindow(rolePanel);
}

function callRoleKecamatanGrid(id){
	var newRow = false;
	var roleKecamatanGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	Ext.define('RoleKecamatan',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'grupPenggunaId',
			type : 'string',
		},
		{
			name : 'username',
			type : 'string',
		}
        ]
	});
	
	roleKecamatanStore = Ext.create('Ext.data.Store', {
		model : 'RoleKecamatan',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../GrupPenggunaController?command=load&type=grid&select=kecamatan&id='+id+"&kecamatanID="+kecamatanID,
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	roleKecamatanGrid = Ext.create('Ext.grid.Panel', {
	    store: roleKecamatanStore,
	    anchor:'100% 100%',
	    columns: [
            { header: 'Grup Pengguna ID',  dataIndex: 'grupPenggunaId', hideable:false, hidden:true},
	        { header: 'Username',  dataIndex: 'username',editor:{
	        	xtype : 'combobox',
	        	store: initAvailableUser(),
	            displayField: 'username',
	            valueField: 'username',
	            editable : false
	        }}
	    ],
	    forceFit : true,
	    plugins:[roleKecamatanGridEditing],
	    bbar : Ext.create('Ext.PagingToolbar', {
            store: roleDUKCAPILStore,
            displayInfo: true,
            emptyMsg: "No topics to display",
            items:[
					{
						  text : 'Tambah',
						  iconCls : 'add',
						  handler : function(){
							  roleKecamatanGridEditing.cancelEdit();
							  var r = Ext.create('RoleKecamatan', {
					        	  username: 'Silakan pilih'
					          });
					          roleKecamatanStore.insert(0, r);
					          roleKecamatanGridEditing.startEdit(0, 0);
					          newRow = true;
						  }
					},
					{
						  text : 'Hapus',
						  iconCls : 'delete',
						  handler: function(){
							  postDeleteRoleKecamatan();
						  }
					}
            ]
	    })
	});
	
	roleKecamatanGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        roleKecamatanStore.removeAt(0);
	    newRow = false;
	});
	
	roleKecamatanGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    username = o.record.get("username");
	    if(newRow == true){
		    postAddRoleKecamatan();
		    newRow = false;
	    }
	    else{
	    	
	    }
	},this);
	
	roleKecamatanGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			username = selectedRecord[0].data.username;
			grupPenggunaID = selectedRecord[0].data.grupPenggunaId;
		}
		catch(err){
			
		}
	});
	
	return roleKecamatanGrid;
}

function callRoleDUKCAPILGrid(id){
	var newRow = false;
	var roleDUKCAPILGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	Ext.define('RoleDUKCAPIL',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'grupPenggunaId',
			type : 'string',
		},
		{
			name : 'username',
			type : 'string',
		}
        ]
	});
	
	roleDUKCAPILStore = Ext.create('Ext.data.Store', {
		model : 'RoleDUKCAPIL',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../GrupPenggunaController?command=load&type=grid&select=DUKCAPIL&id='+id,
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	roleDUKCAPILGrid = Ext.create('Ext.grid.Panel', {
	    store: roleDUKCAPILStore,
	    anchor:'100% 100%',
	    columns: [
            { header: 'Grup Pengguna ID',  dataIndex: 'grupPenggunaId', hideable:false, hidden:true},
	        { header: 'Username',  dataIndex: 'username',editor:{
	        	xtype : 'combobox',
	        	store: initAvailableUser(),
	            displayField: 'username',
	            valueField: 'username',
	            editable : false
	        }}
	    ],
	    forceFit : true,
	    plugins:[roleDUKCAPILGridEditing],
	    bbar : Ext.create('Ext.PagingToolbar', {
            store: roleDUKCAPILStore,
            displayInfo: true,
            emptyMsg: "No topics to display",
            items:[
					{
						  text : 'Tambah',
						  iconCls : 'add',
						  handler : function(){
							  roleDUKCAPILGridEditing.cancelEdit();
							  var r = Ext.create('RoleDUKCAPIL', {
					        	  username: 'Silakan pilih'
					          });
					          roleDUKCAPILStore.insert(0, r);
					          roleDUKCAPILGridEditing.startEdit(0, 0);
					          newRow = true;
						  }
					},
					{
						  text : 'Hapus',
						  iconCls : 'delete',
						  handler: function(){
							  postDeleteRoleDUKCAPIL();
						  }
					}
            ]
	    })
	});
	
	roleDUKCAPILGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        roleDUKCAPILStore.removeAt(0);
	    newRow = false;
	});
	
	roleDUKCAPILGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    username = o.record.get("username");
	    if(newRow == true){
		    postAddRoleDUKCAPIL();
		    newRow = false;
	    }
	    else{
	    	
	    }
	},this);
	
	roleDUKCAPILGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			username = selectedRecord[0].data.username;
			grupPenggunaID = selectedRecord[0].data.grupPenggunaId;
		}
		catch(err){
			
		}
	});
	
	return roleDUKCAPILGrid;
}

function callRoleKelurahanGrid(id){
	var newRow = false;
	var roleKelurahanGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	Ext.define('RoleKelurahan',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'grupPenggunaId',
			type : 'string',
		},
		{
			name : 'username',
			type : 'string',
		}
        ]
	});
	
	roleKelurahanStore = Ext.create('Ext.data.Store', {
		model : 'RoleKelurahan',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../GrupPenggunaController?command=load&type=grid&select=kelurahan&id='+id+"&kelurahanID="+kelurahanID,
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	roleKelurahanGrid = Ext.create('Ext.grid.Panel', {
	    store: roleKelurahanStore,
	    anchor:'100% 100%',
	    columns: [
            { header: 'Grup Pengguna ID',  dataIndex: 'grupPenggunaId', hideable:false, hidden:true},
	        { header: 'Username',  dataIndex: 'username',editor:{
	        	xtype : 'combobox',
	        	store: initAvailableUser(),
	            displayField: 'username',
	            valueField: 'username',
	            editable : false
	        }}
	    ],
	    forceFit : true,
	    plugins:[roleKelurahanGridEditing],
	    bbar : Ext.create('Ext.PagingToolbar', {
            store: roleDUKCAPILStore,
            displayInfo: true,
            emptyMsg: "No topics to display",
            items:[
					{
						  text : 'Tambah',
						  iconCls : 'add',
						  handler : function(){
							  roleKelurahanGridEditing.cancelEdit();
							  var r = Ext.create('RoleKelurahan', {
					        	  username: 'Silakan pilih'
					          });
					          roleKelurahanStore.insert(0, r);
					          roleKelurahanGridEditing.startEdit(0, 0);
					          newRow = true;
						  }
					},
					{
						  text : 'Hapus',
						  iconCls : 'delete',
						  handler: function(){
							  postDeleteRoleKelurahan();
						  }
					}
            ]
	    })
	});
	
	roleKelurahanGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	        roleKelurahanStore.removeAt(0);
	    newRow = false;
	});
	
	roleKelurahanGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    username = o.record.get("username");
	    if(newRow == true){
		    postAddRoleKelurahan();
		    newRow = false;
	    }
	    else{
	    	
	    }
	},this);
	
	roleKelurahanGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			username = selectedRecord[0].data.username;
			grupPenggunaID = selectedRecord[0].data.grupPenggunaId;
		}
		catch(err){
			
		}
	});
	
	return roleKelurahanGrid;
}

//OTHER CITY 
function callKotaGrid(){
	var newRow = false;
	var kotaGridEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	    clicksToMoveEditor: 1,
	    autoCancel: false
	});
	
	Ext.define('Kota',{
		extend : 'Ext.data.Model',
		fields : [
		{
			name : 'kotaId',
			type : 'string',
		},
		{
			name : 'username',
			type : 'string',
		},
		{
			name : 'nama',
			type : 'string',
		},
		{
			name : 'sandi',
			type : 'string',
		},
		{
			name : 'waktuLogin',
			type : 'string',
		},
		{
			name : 'aktif',
			type : 'bool',
		},
        ]
	});
	
	kotaStore = Ext.create('Ext.data.Store', {
		model : 'Kota',
		pageSize : 12,
		proxy : {
			type: 'ajax',
	        url : '../KotaController?command=load',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	kotaGrid = Ext.create('Ext.grid.Panel', {
	    store: kotaStore,
	    anchor:'100% 100%',
	    columns: [
			{ header: 'ID Kota', dataIndex: 'kotaId',hideable:false, hidden:true},
			{ header: 'Nama',  dataIndex: 'nama',editor:{
				id:'namaTextField',
				allowBlank :false
			}},
            { header: 'Username',  dataIndex: 'username',editor:{
            	id:'usernameTextField',
            	allowBlank :false
            }},
	        { header: 'Waktu Login', dataIndex: 'waktuLogin'},
	        { header: 'Aktif', dataIndex: 'aktif', xtype:'checkcolumn',editor:{
            	id:'aktifCheckBox',
            	xtype:'checkbox'
            }}
	    ],
	    forceFit : true,
	    plugins:[kotaGridEditing],
	    bbar : Ext.create('Ext.PagingToolbar', {
            store: kotaStore,
            displayInfo: true,
            displayMsg: 'Menampilkan data {0} - {1} dari {2}',
            emptyMsg: "No topics to display",
            items:[
					{
						  text : 'Tambah',
						  iconCls : 'add',
						  handler : function(){
							  kotaGridEditing.cancelEdit();
							  var r = Ext.create('Kota', {
								  nama: 'Nama Kota',
					        	  username: 'Username pengguna',
					        	  aktif:false
					          });
					          kotaStore.insert(0, r);
					          kotaGridEditing.startEdit(0, 0);
					          newRow = true;
						  }
					},
					{
						  text : 'Hapus',
						  iconCls : 'delete',
						  handler: function(){
							  postDeleteKota();
						  }
					}
            ]
	    })
	});
	
	kotaGridEditing.on('canceledit', function(o) {   
	    if(newRow == true)
	    	kotaStore.removeAt(0);
	    newRow = false;
	});
	
	kotaGridEditing.on('edit', function(o) {      
	    o.record.commit();
	    var nama = o.record.get("nama");
	    var username = o.record.get("username");
	    var aktif = Ext.getCmp("aktifCheckBox").getValue();
	    var waktuLogin = o.record.get("waktuLogin");
	    var sandi = o.record.get("sandi");
	    if(newRow == true){
	    	postAddKota(nama, username, aktif);
		    newRow = false;
	    }
	    else{
	    	postEditKota(nama, username, waktuLogin, sandi, aktif);
	    }
	},this);
	
	kotaGrid.getSelectionModel().on('selectionChange', function(sm, selectedRecord){
		try{
			kotaID = selectedRecord[0].data.kotaId;
			username = selectedRecord[0].data.username;
		}
		catch(err){
			
		}
	});
	
	doLayoutWindow(kotaGrid);
}

//POST TO SERVER
function postAddPengguna(username, nama, aktif){
	Ext.Ajax.request({
        url: '../PenggunaController',
        params: { 
            "command" : "add",
            "username" : username,
            "nama" : nama,
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
        		penggunaStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditPengguna(username, nama, sandi, waktuLogin, ipLogin, aktif){
	Ext.Ajax.request({
        url: '../PenggunaController',
        params: { 
            "command" : "edit",
            "username" : username,
            "nama" : nama,
            "sandi" : sandi,
            "waktuLogin" : waktuLogin,
            "ipLogin" : ipLogin,
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
        		penggunaStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeletePengguna(username){
	Ext.Ajax.request({
        url: '../PenggunaController',
        params: { 
            "command" : "delete",
            "username" : username
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
        		penggunaStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddGrup(nama,aktif){
	Ext.Ajax.request({
        url: '../GrupController',
        params: { 
            "command" : "add",
            "nama" : nama,
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
        		grupStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditGrup(id, nama,aktif){
	Ext.Ajax.request({
        url: '../GrupController',
        params: { 
            "command" : "edit",
            "id" : id,
            "nama" : nama,
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
        		grupStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteGrup(id){
	Ext.Ajax.request({
        url: '../GrupController',
        params: { 
            "command" : "delete",
            "id" : id
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
        		grupStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddRoleDUKCAPIL(){
	Ext.Ajax.request({
        url: '../GrupPenggunaController',
        params: { 
            "command" : "add",
            "column" : "dukcapil",
            "grupID" : grupID,
            "username" : username
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
        		roleDUKCAPILStore.load(function(records, operation, success){
        			
        		});
        		availableUserStore.load(function(records, operation, success){
        			
        		});
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteRoleDUKCAPIL(){
	Ext.Ajax.request({
        url: '../GrupPenggunaController',
        params: { 
            "command" : "delete",
            "column" : "dukcapil",
            "username" : username,
            "grupPenggunaID" : grupPenggunaID
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
        		roleDUKCAPILStore.load(function(records, operation, success){
        			
        		});
        		availableUserStore.load(function(records, operation, success){
        			
        		});
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddRoleKecamatan(){
	Ext.Ajax.request({
        url: '../GrupPenggunaController',
        params: { 
            "command" : "add",
            "column" : "kecamatan",
            "grupID" : grupID,
            "kecamatanID" : kecamatanID,
            "username" : username
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
        		roleKecamatanStore.load(function(records, operation, success){
        			
        		});
        		availableUserStore.load(function(records, operation, success){
        			
        		});
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteRoleKecamatan(){
	Ext.Ajax.request({
        url: '../GrupPenggunaController',
        params: { 
            "command" : "delete",
            "column" : "kecamatan",
            "username" : username,
            "grupPenggunaID" : grupPenggunaID
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
        		roleKecamatanStore.load(function(records, operation, success){
        			
        		});
        		availableUserStore.load(function(records, operation, success){
        			
        		});
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddRoleKelurahan(){
	Ext.Ajax.request({
        url: '../GrupPenggunaController',
        params: { 
            "command" : "add",
            "column" : "kelurahan",
            "grupID" : grupID,
            "kelurahanID" : kelurahanID,
            "username" : username
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
        		roleKelurahanStore.load(function(records, operation, success){
        			
        		});
        		availableUserStore.load(function(records, operation, success){
        			
        		});
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteRoleKelurahan(){
	Ext.Ajax.request({
        url: '../GrupPenggunaController',
        params: { 
            "command" : "delete",
            "column" : "kelurahan",
            "username" : username,
            "grupPenggunaID" : grupPenggunaID
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
        		roleKelurahanStore.load(function(records, operation, success){
        			
        		});
        		availableUserStore.load(function(records, operation, success){
        			
        		});
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postAddKota(nama, username, aktif){
	Ext.Ajax.request({
        url: '../KotaController',
        params: { 
            "command" : "add",
            "nama" : nama,
            "username" : username,
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
        		kotaStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postEditKota(nama, username, waktuLogin, sandi, aktif){
	Ext.Ajax.request({
        url: '../KotaController',
        params: { 
            "command" : "edit",
            "kotaID" : kotaID,
            "nama" : nama,
            "username" : username,
            "waktuLogin" : waktuLogin,
            "sandi" : sandi,
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
        		kotaStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}

function postDeleteKota(){
	Ext.Ajax.request({
        url: '../KotaController',
        params: { 
            "command" : "delete",
            "kotaID" : kotaID,
            "username" : username
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
        		kotaStore.load(function(records, operation, success) {
                });
        	}
        },
        failure: function(response, opts){

        }
    });
}