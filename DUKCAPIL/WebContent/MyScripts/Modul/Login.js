var loginForm = Ext.create('Ext.form.Panel', {
	url : '',
	frame : true,
	defaultType : 'textfield',
	items : [ {
		fieldLabel : 'Username',
		id : 'usernameTextField',
		allowBlank : false
	}, {
		fieldLabel : 'Sandi',
		id : 'sandiTextField',
		inputType:'password',
		allowBlank : false
	}],
	buttons : [ {
		text : 'Login',
		handler:function(){
			Ext.Ajax.request({
		        url: 'LoginController',
		        params: { 
		            "username" : Ext.getCmp("usernameTextField").getValue(),
		            "sandi" : Ext.getCmp("sandiTextField").getValue()
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
		        		window.location = "Main.jsp";
		        	}
		        },
		        failure: function(response, opts){

		        }
		    });
		}
	}, {
		text : 'Clear'
	} ]
});

var loginWindow = Ext.widget('window', {
    title: '<center>::Login::<center>',
    closeAction: 'destroy',
    width: 300,
    layout: 'fit',
    items: loginForm,
    draggable : false,
    closable : false
});
