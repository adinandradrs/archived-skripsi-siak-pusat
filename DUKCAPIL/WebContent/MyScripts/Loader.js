Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', '/DUKCAPIL/ExtJS4/pluggin/ux');
Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.ux.grid.FiltersFeature',
    'Ext.toolbar.Paging'
]);
