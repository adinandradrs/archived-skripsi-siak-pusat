//GLOBAL VARIABEL
var bioGrid;
var bioStore;

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
			name : 'kecamatan',
			type : 'string'
		},
		{
			name : 'kelurahan',
			type : 'string'
		}
        ]
	});
	
	bioStore = Ext.create('Ext.data.Store', {
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
	
	var groupFeature = Ext.create('Ext.grid.feature.Grouping',{
        groupHeaderTpl: '{name} ({rows.length} data)'
    });
	
	bioGrid = Ext.create('Ext.grid.Panel',{
		store: bioStore,
	    anchor:'100% 100%',
	    columns: [
            {header:'NIK', dataIndex:'nik',groupable:false,filter: {
	            type: 'string'   
            }},
            {header:'Nama', dataIndex:'nama',groupable:false},
            {header:'Jenis Kelamin', dataIndex:'jenisKelamin'},
            {header:'Tanggal Lahir', dataIndex:'tanggalLahir',groupable:false},
            {header:'Tempat Lahir', dataIndex:'tempatLahir',groupable:false},
            {header:'Kelurahan', dataIndex:'kelurahan'},
            {header:'Kecamatan', dataIndex:'kecamatan'}
	    ],
	    features: [groupFeature, filters],
	    plugins: [{
            ptype: 'rowexpander',
            rowBodyTpl : [
                '<div style="float:left"><b>Pendidikan:</b> {pendidikan}<br>',
                '<b>Pekerjaan:</b> {pekerjaan}<br>',
                '<b>Akta Kelahiran:</b> {aktaKelahiran}<br>',
                '<b>No. KK:</b> {kartuKeluarga}<br>',
                '<b>Agama:</b> {agama}<br>',
                '<b>Golongan Darah:</b> {golonganDarah}<br></div>',
                '<div style="float:left; margin-left:70px;">{foto}<br>'
            ]
        }],
	    forceFit : true
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
