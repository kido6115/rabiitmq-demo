<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="/sockjs-0.3.4.js"></script>
    <script src="/stomp.js"></script>
    <script type="text/javascript">
        var stompClient = null;
        var userName = null;
        var sessionId = null;

        function setConnected(connected) {
            document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('conversationDiv').style.visibility
                = connected ? 'visible' : 'hidden';
            document.getElementById('response').innerHTML = '';

            document.getElementById('from').disabled = connected;
        }

        function connect() {
            var socket = new SockJS('/chatroom');
            stompClient = Stomp.over(socket);

            userName = document.getElementById('from').value;
            stompClient.connect({user: userName}, function (frame) {
                setConnected(true);
                console.log('Connected: ' + frame);

                // 廣播
                stompClient.subscribe('/topic/messages', function (messageOutput) {
                    showMessageOutput(JSON.parse(messageOutput.body));
                });

                // 私人
                stompClient.subscribe('/user/a/subscribe', function (messageOutput) {
                    showMessageOutput(JSON.parse(messageOutput.body));
                });

            });

        }

        function disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            setConnected(false);
            userName = null;
            console.log("Disconnected");
        }

        function sendMessage() {
            var from = document.getElementById('from').value;
            var text = document.getElementById('text').value;
            if (text != '') {
                stompClient.send("/app/chat", {}, JSON.stringify({'from': from, 'text': text}));
                document.getElementById('text').value = '';
            }
        }

        function showMessageOutput(messageOutput) {
            var response = document.getElementById('response');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.appendChild(document.createTextNode(messageOutput.message.from + " (" + messageOutput.dateStr + ")" + ": "
                + messageOutput.message.text));
            response.appendChild(p);

            var elem = document.getElementById('scroll');
            elem.scrollTop = elem.scrollHeight;
        }

    </script>
</head>
<body onload="disconnect()">
<div>
    <div>
        <input type="text" id="from" placeholder="Choose a nickname"/>
    </div>
    <br/>
    <div>
        <button id="connect" onclick="connect();">Connect</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
    </div>
    <br/>
    <div id="conversationDiv">
        <input type="text" id="text" placeholder="Write a message..."/>
        <button id="sendMessage" onclick="sendMessage();">Send</button>
    </div>
    <div id="scroll" style="height:600px;width:500px;border:1px;font:16px/26px Georgia, Garamond, Serif;overflow:auto;">
        <p id="response"></p>
    </div>
</div>
</body>
</html>