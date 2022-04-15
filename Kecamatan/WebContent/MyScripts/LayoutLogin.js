var monitorWidth = screen.width / 2 - 150;
var monitorHeight = screen.height / 2 - 150;

Ext.onReady(function(){
	
	Ext.create('Ext.container.Viewport', {
		layout : 'border',
		items : [{
			region : 'center',
			title : '<center>Kecamatan Operating System</center>',
			html : '<div class="main"></div>'
		}]
	});
	
	loginWindow.setPosition(monitorWidth,monitorHeight,true);
	loginWindow.show();
	
});