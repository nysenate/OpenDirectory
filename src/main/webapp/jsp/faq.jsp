<%@ page language="java" import="gov.nysenate.opendirectory.utils.UrlMapper"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");

%><jsp:include page="header.jsp" />		
			<div id="main">
				
				<h2 style="margin-left:30px;">Open Directory FAQ</h2>
				
				<div style="margin-left:30px;">
					<ol>
						<li><a href="#faq_what">What should I put on my profile?</a></li>
						<li><a href="#faq_login">What's my username and password?</a></li>
						<li><a href="#faq_information">How can I decide what information people see?</a></li>
						<li><a href="#faq_privacy">What are the different privacy settings?</a></li>
						<li><a href="#faq_browse">Why can't I see the browse by location link?</a></li>
						<li><a href="#faq_search">How do the search results work?</a></li>
						<li><a href="#faq_auto">How does the auto-suggest on the front page work?</a></li>
						<li><a href="#faq_update">How are the "Updated Profiles" on the front page selected?</a></li>
						<li><a href="#faq_vcard">What is a vCard?</a></li>
						<li><a href="#faq_contact">Where can I give feedback or ideas?</a></li>
					</ol>
				</div>
 
				<h3 id="faq_what" style="margin-left:30px;">1. What should I put on my profile?</h3>
				<p class="faq_entry">
					For a few examples of how you can make the most of your profile, have a look at some of 
					our team!
					 
					<a href="/opendirectory/person/richard/profile">Sam Richard</a>
					<a href="/opendirectory/person/williams/profile">Jared Williams</a>
					<a href="/opendirectory/person/yee/profile">Benjamin Yee</a>
				</p>
				
				<h3 id="faq_login" style="margin-left:30px;">2. What's my username and password?</h3>
				<p class="faq_entry">
					Your username and password are the username and "p" password that STS gave you when you 
					joined the Senate. It's the same one that gets you into SenateOnline and your Senate email.
				</p>
				
				<h3 id="faq_information" style="margin-left:30px;">3. How can I decide what information people see?</h3>
				<p class="faq_entry">
					Once you log in, go to "Edit Profile" in the upper the right hand corner of any Open Directory 
					page. At the top of the brown section are links to "Profile" and "Settings", click "Settings". 
					Here you can change the privacy options for every field in your profile. You can alter the 
					privacy settings for every piece of information except your name and picture.
				</p>
				
				<h3 id="faq_privacy" style="margin-left:30px;">4. What are the different privacy settings?</h3>
				<p class="faq_entry">
					<b>Public</b>: Anyone visiting Open Directory can see this information. Currently Open 
					Directory is only available to the State Legislature.
					 
					<b>Senate</b>: Only logged-in, authenticated users, all of whom are Senate employees, 
					can see this information.
					 
					<b>Private</b>: While this information will be saved on your edit screen, it will not appear 
					on your profile at all.
				</p>
				
				<h3 id="faq_browse" style="margin-left:30px;">5. Why can't I see the browse by location link?</h3>
				<p class="faq_entry">
					Because Location is a field for which people can select their privacy and all such fields 
					joined are set to "Senate" by default, only people who are logged in may access that data 
					or browse it.
				</p>
				
				<h3 id="faq_search" style="margin-left:30px;">6. How do the search results work?</h3>
				<p class="faq_entry">
					<i><b>For a single word</b></i>
					When you search Open Directory first looks for a perfect match. If it finds one, then it will 
					bring back those results. If it doesn't it will automatically search for variations of the 
					word (e.g., files starting with those letters) and bring them back in order of how closely 
					they resemble your search term.
					 
					<i><b>For multiple words</b></i>
					Open Directory will return perfect matches for your search at the top of the list. It will 
					then attempt to return matches for files which contain a combination of multiple terms and 
					then files matching any one of your terms.
					 
					For example, a search for Chief Information Officer's Office first returns every person with 
					"Chief Information Officer's Office" in their profile. Underneath that it will return 
					results for people with the term "Chief", "Information", "Officer's" or "Office", as well as 
					combinations of those terms.
					 
					The most relevant searches will be returned at the top of the page. Partial searches can be 
					viewed instantly by clicking the link at the bottom of the page.
				</p>
				
				<h3 id="faq_auto" style="margin-left:30px;">7. How does the auto-suggest on the front page work? Why doesn't it 
				complete my sentence like Google?</h3>
				<p class="faq_entry">
					When you start typing Open Directory starts looking for people, not words. As a result, when you 
					type something into the search bar, it doesn't suggest the rest of what you're looking for, it 
					suggests people who have what you're typing in their profile. 
					 
					For example, if you're looking for people who know HTML, when you start typing "HTM" Open 
					Directory won't suggest "L", it will suggest people who have the term "HTM" somewhere in their 
					profile - be it in interests, skills, title, name, etc.
				</p>
				
				<h3 id="faq_update" style="margin-left:30px;">8. How are the "Updated Profiles" on the front page selected?</h3>
				<p class="faq_entry">
					The profiles on the front page are users who have given permission to the Open Directory team 
					to display them as examples of completed profiles. These profiles are to help other people see 
					who's getting involved in Open Directory as well as give people ideas on how to fill out their 
					profiles.
				</p>
				
				<h3 id="faq_vcard" style="margin-left:30px;">9. What is a vCard?</h3>
				<p  class="faq_entry">
					vCards are electronic business cards. They can be downloaded and placed into an address book 
					program on your computer or phone and will add that contact with any information on the card. vCard 
					information can include text as well as photos and audio.
				</p>
				
				<h3 id="faq_contact" style="margin-left:30px;">10. Where can I give feedback or ideas?</h3>
				<p class="faq_entry">
					<a href="http://www.nysenate.gov/contact">http://nysenate.gov/contact</a>
				</p>
			</div>
			<div id="footer">
			</div>
<jsp:include page="footer.jsp" />