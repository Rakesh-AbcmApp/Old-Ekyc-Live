function ajaxindicatorstart(text) {
    if (jQuery('body').find('#resultLoading').length === 0) {
        jQuery('body').append(`
            <div id="resultLoading" style="display:none">
                <div class="modern-loader">
                    <div class="spinner"></div>
                    <div class="loader-text">${text}</div>
                </div>
                <div class="bg"></div>
            </div>
        `);
    }

    jQuery('#resultLoading').css({
        'width': '100%',
        'height': '100%',
        'position': 'fixed',
        'z-index': '10000000',
        'top': '0',
        'left': '0',
        'display': 'flex',
        'justify-content': 'center',
        'align-items': 'center',
        'flex-direction': 'column'
    });

    jQuery('#resultLoading .bg').css({
        'background': 'rgba(0,0,0,0.6)',
        'width': '100%',
        'height': '100%',
        'position': 'absolute',
        'top': '0',
        'left': '0',
        'z-index': '-1'
    });

    jQuery('.modern-loader').css({
        'text-align': 'center',
        'color': '#ffffff',
        'font-size': '18px',
        'font-weight': '500',
        'z-index': '10'
    });

    jQuery('.loader-text').css({
        'margin-top': '10px',
        'color': '#fff',
        'text-shadow': '0 0 2px #000'
    });

    // Inject spinner style if not already present
    if (jQuery('#spinner-style').length === 0) {
        jQuery('head').append(`
            <style id="spinner-style">
                .spinner {
                    width: 50px;
                    height: 50px;
                    border: 6px solid #ccc;
                    border-top-color: #00d1b2;
                    border-radius: 50%;
                    animation: spin 0.8s linear infinite;
                    margin: 0 auto;
                }
                @keyframes spin {
                    to { transform: rotate(360deg); }
                }
            </style>
        `);
    }

    jQuery('#resultLoading').fadeIn(200);
    jQuery('body').css('cursor', 'wait');
}

function ajaxindicatorstop() {
    jQuery('#resultLoading').fadeOut(300, function () {
        jQuery(this).remove();
    });
    jQuery('body').css('cursor', 'default');
}
