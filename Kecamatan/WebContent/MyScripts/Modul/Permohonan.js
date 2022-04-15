var permohonanTree;

var permohonanStore;

Ext.onReady(function(){
	
	Ext.define('Permohonan', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'permohonanID', type:'string'},
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
	
	var permohonanTree = Ext.create('Ext.tree.Panel', {
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
        }]
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