
(function () {


	// Your web app's Firebase configuration
	var firebaseConfig = {
		apiKey: "AIzaSyAn6pxLwnKMjY7j5cGCrERGGE2AmqskUBI",
		authDomain: "unitunes-802ed.firebaseapp.com",
		databaseURL: "https://unitunes-802ed.firebaseio.com",
		projectId: "unitunes-802ed",
		storageBucket: "unitunes-802ed.appspot.com",
		messagingSenderId: "382404032196",
		appId: "1:382404032196:web:1e2e90115272087713bb91",
		measurementId: "G-4WN7JL3WCD"
	};

	// Initialize Firebase

	firebase.initializeApp(firebaseConfig);
	firebase.analytics();

	//get the elements.

	const emailText = document.getElementById('emailText');
	const passwordText = document.getElementById('passwordText');
	const loginButton = document.getElementById('loginButton');
	const signupButton = document.getElementById('signupButton');
	const logoutButton = document.getElementById('logoutButton');

	loginButton.addEventListener('click', e => {
		const email = emailText.value;
		const password = passwordText.value;
		const authorization = firebase.auth();
		//use this line to sign in
		const valid = authorization.signInWithEmailAndPassword(email, password);
		//Catch if it is invalid.
		valid.catch(e => console.log(e.message));
	});

	signupButton.addEventListener('click', e => {
		const email = emailText.value;
		const password = passwordText.value;
		const authorization = firebase.auth();
		//use this line to sign in
		const valid = authorization.createUserWithEmailAndPassword(email, password);
		//Catch if it is invalid.
		valid.catch(e => console.log(e.message));
	});

	firebase.auth().onAuthStateChanged(firebaseUser => {
		if (firebaseUser) {
			console.log(firebaseUser);
		} else {
			//could use this for hiding the logout button
			console.log('not logged in');
		}
	});

	logoutButton.addEventListener('click', e => {
		firebase.auth().signOut();
	});

	//add event listeners for sign up and log out as well.

})
