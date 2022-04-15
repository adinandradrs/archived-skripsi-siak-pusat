Ext.onReady(function(){

	Ext.define('File',{
		extend : 'Ext.data.Model',
		fields : [
			{
				name : 'file',
				type : 'string',
			}
        ]
	});
	
	fileStore = Ext.create('Ext.data.Store', {
		model : 'File',
		proxy : {
			type: 'ajax',
	        url : '../LogController?command=load&select=file',
	        reader: {
	            type: 'json'
	        }
		},
	    autoLoad: true
	});
	
	Ext.create('Ext.container.Viewport', {
	    layout: 'border',
	    items: [{
	        region: 'west',
	        width : 120,
	        layout:'anchor',
	        items:[
	               {
	            	   xtype: 'grid',
	            	   title:'Explorer',
	            	   id:'explorerGrid',
	            	   store:fileStore,
	            	   anchor:'100% 100%',
	                   columns: [
                         	{
                         		header: 'File',
                         		dataIndex : 'file'
                         	}
                       ],
                       forceFit:true
	               }
	    ]},
	    {
	        region: 'center',
	        layout:'anchor',
	        items:[
	               {
	            	   id: 'notepad',
	            	   xtype: 'htmleditor',
	            	   anchor:'100% 100%'
	               }
	        ]
	    
	    }]
	});
	
	Ext.getCmp('explorerGrid').getSelectionModel().on('selectionchange', function(sm, selectedRecord){
		Ext.Ajax.request({
	        url: '../LogController',
	        params: { 
	        	'command' : 'load',
	        	'select' : 'log',
	        	'file':selectedRecord[0].data.file
	        },
	        success: function(response, opts){
	        	var obj = Ext.JSON.decode(response.responseText);
	        	if(obj.success == false){
	        			
	        	}
	        	else{
	        		Ext.getCmp('notepad').setValue(obj.log);
	        	}
	        },
	        failure: function(response, opts){

	        }
	    });
	});
	
});