@(implicit r: RequestHeader)

$(function() {

    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
    var gameSocket = new WS("@routes.Game.game().webSocketURL()")

    var receiveEvent = function(event) {
        if (event.data != '') {
            var el = $('<div class="message"></div>')
            $('#gamemessages').append(el)
            $(el).append(event.data)
            $('#scroll').scrollTop(100000)
        }
    }

    $("#gamestart").click(function() {
        $.ajax({
            type: "GET",
            url: "/game/start",
            dataType: "text",
            success: function(data) {
                $("#plist").html(data)
            }
        })
    })

    $("#gamerestart").click(function() {
        $.ajax({
            type: "GET",
            url: "/game/restart",
            data: {id: $("#plist").val()}
        })
    })

    $("#fillstates").click(function() {
        $.ajax({
            type: "GET",
            url: "/game/fill",
            dataType: "text",
            success: function(data) {
                $("#plist").html(data)
            }
        })
    })

    gameSocket.onmessage = receiveEvent

})
