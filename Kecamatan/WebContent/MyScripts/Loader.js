Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', '/Kecamatan/ExtJS4/pluggin/ux');
Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.ux.grid.FiltersFeature',
    'Ext.toolbar.Paging'
]);
