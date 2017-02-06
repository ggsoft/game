@(implicit r: RequestHeader)

$(function() {

    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
    var chatSocket = new WS("@routes.Chat.chat().webSocketURL()")

    var sendMessage = function(mes) {
        chatSocket.send(JSON.stringify(
            {text: mes}
        ))
    }

    var receiveEvent = function(event) {
        var data = JSON.parse(event.data)

        if(data.error) {
            chatSocket.close()
            addNotice(data.error)
            return
        } else {
            $("#onChat").show()
        }

        if (data.kind == 'ping') {
            return
        }

        if ((data.kind != 'info')&&(data.message != '')) {
            var el = $('<div class="message"><span title="'+data.date+'"></span></div>')
            $("span", el).text(data.user)
            $('#chatmessages').append(el)
            $(el).append(data.message)
            $('#scroll').scrollTop(9999)
        }
        $("#members").html(data.members)
    }

    var handleReturnKey = function(e) {
        if (e.ctrlKey) {
           if ((e.keyCode == 10) || (e.keyCode == 13)) {
               e.preventDefault();
               send();
           }
        }
    }

    $("#talk").keydown(handleReturnKey)

    $("#chatsend").click(function() {
        send();
    })

    function send() {
        if ($("#talk").val().trim()!='') {
            sendMessage($("#talk").val());
            $("#talk").val('')
        }
    }

    chatSocket.onmessage = receiveEvent

})
