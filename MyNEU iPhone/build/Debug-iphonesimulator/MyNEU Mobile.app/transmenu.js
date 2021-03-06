function transMenu() {
	/* ACCOUNTS MENU */
	document.body = document.createElement('body'); 
	document.body.style.margin = '20px'; 
	links = []; 
	urls = {
			'Husky Dollars': 'https://prod-web.neu.edu/webapp6/ISF/cardTxns.do?view=svc&page=0&sort=', 
			'Dining Dollars': 'https://prod-web.neu.edu/webapp6/ISF/cardTxns.do?view=meal&page=0&sort=', 
			'Laundry Bucks': 'https://prod-web.neu.edu/webapp6/ISF/cardTxns.do?view=laundry&page=0&sort=', 
			'Print Allowance': 'https://prod-web.neu.edu/webapp6/ISF/cardTxns.do?view=printing&page=0&sort=',
	};
	// write new content
	for(var i in urls) {
		links.push(document.createElement('a'));
		links[links.length-1].href = urls[i];
		links[links.length-1].innerHTML = '<div style="width:100%; height: 50px; background-color: lightgrey; text-align: center; line-height: 50px; margin-bottom: 10px;">' + i + '</div>';
		document.body.appendChild(links[links.length-1]);
	}
	// Add space for the logout button
	document.body.appendChild(document.createElement('br'));
	var back = 'http://myneu.neu.edu/render.userLayoutRootNode.uP?uP_root=root';
	var lo = document.createElement('a');
	lo.href = back;
	lo.innerHTML = '<div style="width:100%; height: 50px; border: 1px solid red; text-color: black; text-align: center; line-height: 50px; margin-bottom: 10px;">Back</div>';
	document.body.appendChild(lo);
}
transMenu();