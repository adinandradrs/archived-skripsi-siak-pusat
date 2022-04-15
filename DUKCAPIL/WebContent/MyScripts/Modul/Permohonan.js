var permohonanTree;

var permohonanStore;

var permohonanRoot;
var permohonanDetailID;

Ext.onReady(function(){
	
	Ext.define('Permohonan', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'permohonanID', type:'string'},
            {name: 'nik', type:'string'},
            {name: 'jenisPermohonan', type:'string'},
            {name: 'tanggalPengajuan', type: 'string'},
            {name: 'kecamatan', type: 'string'},
            {name: 'kelurahan', type: 'string'}
        ]
    });
	
	permohonanStore = Ext.create('Ext.data.TreeStore', {
		proxy: {
			type: 'ajax',
			url: '../PermohonanController?command=load'
		},
		model: 'Permohonan',
		root: {
            name: 'Permohonan',
            id: '0',
            iconCls : 'salatiga'
        },
		folderSort: true
	});
	
	permohonanTree = Ext.create('Ext.tree.Panel', {
        title: 'Data Permohonan',
        anchor:'100% 100%',
        useArrows: true,
        rootVisible: true,
        store: permohonanStore,
        singleExpand: true,
        columns: [{
            xtype: 'treecolumn', 
            text: 'Permohonan',
            flex: 1,
            sortable: true,
            dataIndex: 'jenisPermohonan'
        },{
        	text: 'NIK',
            flex: 1,
            dataIndex: 'nik',
            sortable: true
        },{
            text: 'Tanggal Pengajuan',
            flex: 1,
            dataIndex: 'tanggalPengajuan',
            sortable: true
        },{
            text: 'Kecamatan',
            flex: 1,
            dataIndex: 'kecamatan',
            sortable: true
        },{
        	text: 'Kelurahan',
            flex: 1,
            dataIndex: 'kelurahan',
            sortable: true
        }],
        tbar:[
              	{
              		text:'Hapus',
              		iconCls:'delete',
              		handler:function(){
              			if(permohonanRoot != "undelete"){
              				postDeletePermohonan();
              			}
              			else{
              				Ext.MessageBox.show({
            				  modal : false,
            		          title: 'Peringatan',
            		          msg: 'Folder permohonan tidak dapat dihapus, hanya file permohonan yang dapat dihapus',
            		          buttons: Ext.MessageBox.OK,
            		          icon: Ext.MessageBox.ERROR
            		      	});
              			}
              		}
              	}
              
              ]
	
    });
	
	permohonanTree.getSelectionModel().on('selectionchange', function(sm, selectedRecord){
		try{
			if(selectedRecord[0].data.iconCls == 'folder'){
				permohonanRoot = "undelete";
			}
			else if(selectedRecord[0].data.iconCls == 'file'){
				permohonanRoot = "delete";
				permohonanDetailID = selectedRecord[0].data.permohonanID;
			}
		}
		catch(err){
			
		}
	});
	
	Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [{
	        region: 'center',
	        layout:'anchor',
	        id:'permohonanPanel',
	        items:[permohonanTree]
	    }]
	});
	
});

function postDeletePermohonan(){
	Ext.Ajax.request({
        url: "../PermohonanController",
        params: { 
            "command" : "delete",
            "permohonanDetailID" : permohonanDetailID
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
        		permohonanTree.getStore().load();
        	}
        },
        failure: function(response, opts){

        }
    });
}