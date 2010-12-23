<%@ page language="java" import="gov.nysenate.opendirectory.utils.UrlMapper"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");

%><jsp:include page="header.jsp" />		
			<div id="main">
				<h2 style="margin-left:30px;">About OpenDirectory</h2>
				<p style="margin-left:-70px;">
					OpenDirectory is a new way of finding the people you're looking for who work for the New York<br/>
					State Senate. With a profile for every employee, OpenDirectory automatically displays name,<br/>
					title, office/department, and work contact information within an online, searchable website. From<br/>
					there, employees can edit and augment their own profiles, adding a profile photo, additional<br/>
					contact information and social network information, as well as their skills and interests.
				</p>
				<p style="margin-left:-70px;">
					As with the Senate's <a href="http://www.nysenate.gov/opendata">online payroll reports</a>, only each employee's name, title and department<br/>
					information is 'publicly' visible, and initially this "public" information and the OpenDirectory site<br/>
					itself is only accessible from inside the Senate or other New York State Government computer<br/>
					networks.  Beyond that basic information, each employee has full control over whether they wish<br/>
					to make any other piece of information (e.g.: their phone number, their profile photo, etc.) private,<br/>
					visible only to other Senate employees, or publicly visible to other State government employees.
				</p>
				<p style="margin-left:-70px;">
					With features like skill and interest searches, OpenDirectory makes it easy to find employees<br/>
					who can help you solve a problem or answer a question.  And profile bookmarking makes it easy<br/>
					to save that contact for later.  
				</p>
				<p style="margin-left:-70px;">
					Above all, OpenDirectory is a tool that's meant to highlight the employee community of the State<br/>
					Senate, and bring it together by helping staff to more easily get in touch with and recognize their<br/>
					colleagues who are working all around our great State.  We hope that this will help employees to<br/>
					collaborate better with each other and, where appropriate, with the public, thus making the<br/>
					Senate a more efficient and effective part of our State government.  
				</p>
				<p style="margin-left:-70px;">
					We hope that you'll enjoy this first version of OpenDirectory, and give us feedback at<br/>
					<a href="http://www.nysenate.gov/contact">http://www.nysenate.gov/contact</a> about what features would make OpenDirectory easier to use<br/>
					and more beneficial to your work.
				</p>
			</div>
			<div id="footer">
			</div>
<jsp:include page="footer.jsp" />