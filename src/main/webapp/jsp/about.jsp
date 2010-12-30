<%@ page language="java" import="gov.nysenate.opendirectory.utils.UrlMapper"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");

%><jsp:include page="header.jsp" />		
			<div id="main">
				<h2 style="margin-left:30px;">About OpenDirectory</h2>
				<p class="about">
					OpenDirectory is a new way of finding the people you're looking for who work for the New York 
					State Senate. With a profile for every employee, OpenDirectory automatically displays name, 
					title, office/department, and work contact information within an online, searchable website. 
					From there, employees can edit and augment their own profiles, adding a profile photo, 
					additional contact information and social network information, as well as their skills and 
					interests. For example, check out our Open Directory team of <a href="/opendirectory/person/yee/profile">Ben</a>, <a href="/opendirectory/person/williams/profile">Jared</a> and <a href="/opendirectory/person/richard/profile">Sam</a> and see the 
					sorts of people and skills that are building online tools for the State Senate.
	            </p>
				<p class="about">                  
					As with the Senate's <a href="http://www.nysenate.gov/opendata">online payroll reports</a>, only each employee's name, title and department 
					information is 'publicly' visible, and initially this "public" information and the 
					OpenDirectory site itself is only accessible from inside the Senate or other New York State 
					Government computer networks.  Beyond that basic information, each employee has full control 
					over whether they wish to make any other piece of information (e.g.: their phone number, 
					their profile photo, etc.) private, visible only to other Senate employees, or publicly 
					visible to other State government employees.
	            </p>
				<p class="about">               
					With features like skill and interest searches, OpenDirectory makes it easy to find employees 
					who can help you solve a problem or answer a question.  And profile bookmarking makes it 
					easy to save that contact for later.  
				</p>
				<p class="about">                           
					Above all, OpenDirectory is a tool that's meant to highlight the employee community of the 
					State Senate, and bring it together by helping staff to more easily get in touch with and 
					recognize their colleagues who are working all around our great State.  We hope that this 
					will help employees to collaborate better with each other and, where appropriate, with the 
					public, thus making the Senate a more efficient and effective part of our State government.  
				</p>
				<p class="about">                             
					We hope that you'll enjoy this first version of OpenDirectory, and give us feedback at 
					<a href="http://www.nysenate.gov/contact">http://www.nysenate.gov/contact</a> about what features would make OpenDirectory easier to 
					use and more beneficial to your work.
				</p>
			</div>
			<div id="footer">
			</div>
<jsp:include page="footer.jsp" />