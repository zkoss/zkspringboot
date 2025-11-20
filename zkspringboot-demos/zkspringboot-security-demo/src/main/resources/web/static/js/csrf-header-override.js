/* using zk.augment */
/* chainable, since each override or augment will retrieve the original, or the previous modification if any exists */
zk.afterLoad(function () {
    const {getExtraHeaders} = zk.augment(zAu, {
        getExtraHeaders() {
            const extraHeaders = getExtraHeaders(); //obtain headers map from the original function
        	var token = $("meta[name='_csrf']").attr("content");
        	var header = $("meta[name='_csrf_header']").attr("content");
            extraHeaders[header] = token; //add or replace the header for a given key
            return extraHeaders; //return the modified map
        }
    });
});