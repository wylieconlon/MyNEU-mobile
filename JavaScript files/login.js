// MyNEU Mobile website
// Wylie Conlon and Eric Kelly

// ==UserScript==
// @name           MyNEU Mobile
// @namespace      http://crew.ccs.neu.edu
// @description    Cleans up MyNEU website
// @include        http://myneu.neu.edu/*
// @include        https://myneu.neu.edu/*
// ==/UserScript==

function login() {

	/* Useful URLS:
	Login: http://myneu.neu.edu/cp/home/displaylogin
	Content: render.userLayoutRootNode.uP
	Logged out: jsp/misc/timedout2.jsp
	Transition page: http://myneu.neu.edu/cp/home/logout
	*/	
	
	/* LOGIN FORM
 	USING SCRIPT FROM "MyNEU PROPER LOGIN v0.6" BY brainonfire.net */

    var uuid = /document\.cplogin\.uuid\.value="([0-9a-f-]{36})";/.exec(document.getElementsByTagName('head')[0].innerHTML)[1];

    var submitTo = document.getElementsByName('cplogin')[0].action;
    var submitTo_safe = submitTo.replace(/"/g, '&quot;');
    var properForm =
                                         '<form action="%FormAction%" method="post" id="loginform" onSubmit="showUsername()"> \
                                         <input type="text" name="user" id="username" value="" placeholder="MyNEU Username" style="width: 92%; font-size: 18px; margin-bottom: 5px;"/><br> \
                                         <input type="password" name="pass" placeholder="Password" style="width: 92%; font-size: 18px;"/><br> \
        <input type="hidden" name="uuid" value="%UUID%" /> \
                                         <button style="width:100%; font-size: 18px; margin-top:20px;">Login</button> \
        </form>'.replace('%FormAction%', submitTo_safe).replace('%UUID%', uuid);

    document.body.innerHTML = properForm;
}