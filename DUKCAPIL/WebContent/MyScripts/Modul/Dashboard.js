Ext.onReady(function(){
	
	Ext.define('Permohonan',{
		extend : 'Ext.data.Model',
		fields : [
			{
				name : 'ktp',
				type : 'int'
			},
			{
				name : 'kk',
				type : 'int'
			},
			{
				name : 'an',
				type : 'int'
			},
			{
				name : 'akl',
				type : 'int'
			}
        ]
	});
	
	var permohonanStore = Ext.create('Ext.data.Store', {
		model : 'Permohonan',
		proxy : {
			type: 'ajax',
	        url : '../DashboardController?dash=permohonan',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	Ext.define('Agama',{
		extend : 'Ext.data.Model',
		fields : [
			{
				name : 'nama',
				type : 'string'
			},
			{
				name : 'jumlah',
				type : 'int'
			}
        ]
	});
	
	var agamaStore = Ext.create('Ext.data.Store', {
		model : 'Agama',
		proxy : {
			type: 'ajax',
	        url : '../DashboardController?dash=agama',
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
				name : 'nama',
				type : 'string'
			},
			{
				name : 'jumlah',
				type : 'int'
			}
        ]
	});
	
	var pendudukStore = Ext.create('Ext.data.Store', {
		model : 'Penduduk',
		proxy : {
			type: 'ajax',
	        url : '../DashboardController?dash=penduduk',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	var outerPanel = Ext.create('Ext.panel.Panel',{
		anchor:'100% 100%',
		layout:'anchor',
		tbar:[
		      {
		    	  text:'Refresh',
		    	  handler:function(){

		    		  pendudukStore.load(function(records, operation, success) {
		                });
		    		  agamaStore.load(function(records, operation, success) {
		                });
		    		  permohonanStore.load(function(records, operation, success) {
		                });
		    	  }
		      }
		],
		items:[
		       {
		    	   title:'<center>Permohonan<center>',
		    	   xtype:'panel',
		    	   anchor:'100%',
		    	   layout:'hbox',
		    	   align: 'stretch',
		    	   items:[
		    	          {
		    	        	xtype:'panel',
		    	        	title:'KTP',
		    	        	flex:1,
		    	        	items:[
									{
									   	xtype: 'chart',
									   	width:200,
									   	height:150,
									   	store: permohonanStore,
										animate: {
									    easing: 'bounceOut',
									    duration: 500
									},
									   	axes: [{
									   		type: 'gauge',
									   		position: 'gauge',
									   		minimum: 0,
									   		maximum: 100,
									   		steps: 10,
									   		margin: -10
									   	}],
									   	series: [{
									   		type: 'gauge',
									   		field: 'ktp',
									   		donut: 30,
									   		colorSet: ['#F49D10', '#ddd']
									   }]
									}
		    	        	]
		    	          
		    	          },
		    	          {
			    	        	xtype:'panel',
			    	        	title:'Kartu Keluarga',
			    	        	flex:1,
			    	        	items:[
										{
										   	xtype: 'chart',
										   	width:200,
										   	height:150,
										   	store: permohonanStore,
										   	animate: {
								                easing: 'elasticIn',
								                duration: 1000
								            },
										   	axes: [{
										   		type: 'gauge',
										   		position: 'gauge',
										   		minimum: 0,
										   		maximum: 100,
										   		steps: 10,
										   		margin: -10
										   	}],
										   	series: [{
										   		type: 'gauge',
										   		field: 'kk',
										   		donut: 30,
										   		colorSet: ['#6CB500', '#ddd']
										   }]
										}
			    	        	]
			    	          
		    	          },
		    	          {
			    	        	xtype:'panel',
			    	        	title:'Akta Nikah',
			    	        	flex:1,
			    	        	items:[
										{
										   	xtype: 'chart',
										   	width:200,
										   	height:150,
										   	store: permohonanStore,
										   	animate: {
								                easing: 'elasticIn',
								                duration: 1000
								            },
										   	axes: [{
										   		type: 'gauge',
										   		position: 'gauge',
										   		minimum: 0,
										   		maximum: 100,
										   		steps: 10,
										   		margin: -10
										   	}],
										   	series: [{
										   		type: 'gauge',
										   		field: 'an',
										   		donut: 30,
										   		colorSet: ['#F2F500', '#ddd']
										   }]
										}
								]
		    	          },
		    	          {
			    	        	xtype:'panel',
			    	        	title:'Akta Kelahiran',
			    	        	flex:1,
			    	        	items:[
										{
										   	xtype: 'chart',
										   	width:200,
										   	height:150,
										   	store: permohonanStore,
										   	animate: {
								                easing: 'elasticIn',
								                duration: 1000
								            },
										   	axes: [{
										   		type: 'gauge',
										   		position: 'gauge',
										   		minimum: 0,
										   		maximum: 100,
										   		steps: 10,
										   		margin: -10
										   	}],
										   	series: [{
										   		type: 'gauge',
										   		field: 'akl',
										   		donut: 30,
										   		colorSet: ['#BA22B1', '#ddd']
										   }]
										}
								]
		    	          }
		    	   ]
		       },
		       {
		    	   xtype:'panel',
		    	   anchor:'100%',
		    	   layout:'hbox',
		    	   align: 'stretch',
		    	   items:[
		    	          {
							xtype:'panel',
							title:'Agama',
							flex:1,
							items:[
									{
									   	xtype: 'chart',
									   	width:200,
									   	height:200,
									   	store: agamaStore,
									   	theme: 'Base:gradients',
									   	animate: {
							                easing: 'bounceOut',
							                duration: 1200
							            },
									   	series: [{
									   		type: 'pie',
									   		field: 'jumlah',
									   		donut:20,
									   		showInLegend: true,
									   		label: {
									            field: 'nama',
									            display: 'rotate',
									            contrast: true,
									            font: '12px Arial'
									        }
									   	}]
									}
							]
		    	          },
		    	          {
								xtype:'panel',
								title:'Data Penduduk (Kecamatan)',
								flex:2,
								items:[
										{
										   	xtype: 'chart',
										   	width:600,
										   	height:200,
								            animate: {
								                easing: 'bounceOut',
								                duration: 750
								            },
										   	store:pendudukStore,
										   	axes: [
										           {
										               type: 'Numeric',
										               position: 'left',
										               fields: ['jumlah'],
										               label: {
										                   renderer: Ext.util.Format.numberRenderer('0,0')
										               },
										               grid: true,
										               minimum: 0
										           },
										           {
										               type: 'Category',
										               position: 'bottom',
										               fields: ['nama']
										           }
								            ],
								            series: [
								                     {
								                         type: 'column',
								                         axis: 'left',
								                         label: {
								                           display: 'insideEnd',
								                           'text-anchor': 'middle',
								                             field: 'jumlah',
								                             renderer: Ext.util.Format.numberRenderer('0'),
								                             orientation: 'vertical',
								                             color: '#333'
								                         },
								                         xField: 'nama',
								                         yField: 'jumlah',
								                         renderer: function(sprite, record, attr, index, store) {
								                             var fieldValue = Math.random() * 20 + 10;
								                             var value = (record.get('jumlah') >> 0) % 5;
								                             var color = ['rgb(213, 70, 121)', 
								                                          'rgb(44, 153, 201)', 
								                                          'rgb(146, 6, 157)', 
								                                          'rgb(49, 149, 0)', 
								                                          'rgb(249, 153, 0)'][value];
								                             return Ext.apply(attr, {
								                                 fill: color
								                             });
								                         }
								                     }
							                ]
										}
								]
			    	          }
		    	   ]
		       
		       }
		]
	});
	
	Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [
	    {
	        region: 'center',
	        layout:'anchor',
	        items:[
	               outerPanel
	        ]
	    }]
	});
	
});


