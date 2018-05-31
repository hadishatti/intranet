package qa.tecnositafgulf.viewmodel.companyEvent;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;
import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.model.events.EventComment;
import qa.tecnositafgulf.searchcriteria.companyevents.CompanyEventsSearchCriteria;
import qa.tecnositafgulf.service.EventCommentService;
import qa.tecnositafgulf.service.EventService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.*;


public class ViewEventsViewModel extends IntranetVM{

	private List<CompanyEvent> companyEventModel;
	private Timer timer;
	private boolean viewNewEvent;
	private EventService eventService;
	private EventCommentService eventCommentService;
	private Desktop desktop;
	private Map<CompanyEvent,List<EventComment>> commentList;
	private Map<CompanyEvent, Integer> commentNumbers;
	private CompanyEventsSearchCriteria companyEventsSearchCriteria;
	private Integer totalSize;
	private Integer activePage;


	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		init();
		Double ie = Servlets.getBrowser(super.getRequest(), "ie");
		if (ie != null && ie < 8.0) {
			Clients.showNotification("This demo does not support IE6/7", true);
		}
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		eventService = context.getBean(EventService.class);
		eventCommentService = context.getBean(EventCommentService.class);
		viewNewEvent = super.isPublisher();
		commentList = new HashMap<CompanyEvent, List<EventComment>>();
		commentNumbers = new HashMap<CompanyEvent, Integer>();
		companyEventsSearchCriteria = new CompanyEventsSearchCriteria();
		this.setActivePage(this.companyEventsSearchCriteria.getStartIndex());
		loadEvents();
		updateEventComments();
		desktop = Executions.getCurrent().getDesktop();
		if(timer!=null)
			timer.cancel();
		timer = new Timer();
		timer.schedule(updatePosts(),0,1000);
		addCommonTags((PageCtrl) view.getPage());
	}

	public void loadEvents(){
		this.setTotalSize(this.eventService.getCountCompanyEvents(companyEventsSearchCriteria));
		this.companyEventsSearchCriteria.setStartIndex(getActivePage());
		List<CompanyEvent> companyEvents = eventService.listEvents(companyEventsSearchCriteria);
		for(CompanyEvent companyEvent : companyEvents){
			List<EventComment> eventComments = eventCommentService.listCommentsByEvent(companyEvent);
			companyEvent.setEventComments(eventComments);
			commentList.put(companyEvent, eventComments);
			commentNumbers.put(companyEvent, eventComments.size());
		}
		companyEventModel = companyEvents;
		BindUtils.postNotifyChange(null, null, this, "companyEventModel");
		BindUtils.postNotifyChange(null, null, this, "commentNumbers");
		BindUtils.postNotifyChange(null, null, this, "commentList");
	}


	@Command
	@NotifyChange({"activePage", "totalSize", "companyEventsSearchCriteria", "companyEventModel"})
	public void clearFilters() {
		this.companyEventsSearchCriteria.clear();
		this.companyEventsSearchCriteria.setPageOrderMode("desc");
		this.companyEventsSearchCriteria.clearFilters();
		loadEvents();
	}

	@Command
	@NotifyChange({"activePage", "totalSize", "companyEventsSearchCriteria", "companyEventModel"})
	public void filter() {
		this.companyEventsSearchCriteria.clear();
		this.companyEventsSearchCriteria.setPageOrderMode("desc");
		this.activePage = this.companyEventsSearchCriteria.getStartIndex();
		loadEvents();
	}

	@NotifyChange({"activePage", "totalSize", "companyEventsSearchCriteria", "companyEventModel"})
	public void setActivePage(Integer activePage) {
		this.activePage = activePage;
		loadEvents();
	}

	public void updateEventComments(){
		for(CompanyEvent companyEvent : companyEventModel){
			List<EventComment> eventComments = eventCommentService.listCommentsByEvent(companyEvent);
			companyEvent.setEventComments(eventComments);
			commentList.put(companyEvent, eventComments);
			commentNumbers.put(companyEvent, eventComments.size());
		}
		BindUtils.postNotifyChange(null, null, this, "commentNumbers");
		BindUtils.postNotifyChange(null, null, this, "commentList");
	}

	private int eventSize(){
		List<CompanyEvent> companyEvents = eventService.listEvents(companyEventsSearchCriteria);
		return companyEvents.size();
	}

	private boolean update(){
		if(eventSize()!= companyEventModel.size())
			return true;
		return false;
	}



	@Command
	public void newEvent(){
		Window window = (Window) Executions.createComponents("/pages/editor/companyEvent-editor.zul", null, null);
		window.doModal();
	}

	@Command
	public void editEvent(@BindingParam("companyEvent")CompanyEvent companyEvent){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyEvent", companyEvent);
		Window window = (Window) Executions.createComponents("/pages/editor/companyEvent-editor.zul", null, map);
		window.doModal();

	}

	@Command
	public void deleteEvent(@BindingParam("companyEvent")CompanyEvent companyEvent){
		for(EventComment eventComment : companyEvent.getEventComments()) {
			eventCommentService.removeComment(eventComment);
		}
		companyEvent.getEventComments().removeAll(companyEvent.getEventComments());
		eventService.removeEvent(companyEvent);
	}

	@Command
	@NotifyChange({"commentList"})
	public void comment(@BindingParam("companyEvent") CompanyEvent companyEvent) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyEvent", companyEvent);
		Window window = (Window) Executions.createComponents("/pages/companyEvent/companyEvent-comment.zul", null, map);
		window.doModal();
	}


	private TimerTask updatePosts() {
		return new TimerTask() {
			@Override
			public void run() {
				try {
					if(desktop == null) {
						timer.cancel();
						return;
					}
					Executions.activate(desktop);
					try {
						if(update())
							loadEvents();
						updateEventComments();
					} finally {
						Executions.deactivate(desktop);
					}
				}catch(DesktopUnavailableException ex) {
					System.out.println("Desktop currently unavailable");
					timer.cancel();
				}catch(InterruptedException e) {
					System.out.println("The server push thread interrupted");
					timer.cancel();
				}
			}
		};
	}

	public List<CompanyEvent> getCompanyEventModel() {
		return companyEventModel;
	}

	public void setCompanyEventModel(List<CompanyEvent> companyEventModel) {
		this.companyEventModel = companyEventModel;
	}

	public boolean isViewNewEvent() {
		return viewNewEvent;
	}

	public void setViewNewEvent(boolean viewNewEvent) {
		this.viewNewEvent = viewNewEvent;
	}

	public Map<CompanyEvent, List<EventComment>> getCommentList() {
		return commentList;
	}

	public void setCommentList(Map<CompanyEvent, List<EventComment>> commentList) {
		this.commentList = commentList;
	}

	public Map<CompanyEvent, Integer> getCommentNumbers() {
		return commentNumbers;
	}

	public void setCommentNumbers(Map<CompanyEvent, Integer> commentNumbers) {
		this.commentNumbers = commentNumbers;
	}

	public CompanyEventsSearchCriteria getCompanyEventsSearchCriteria() {
		return companyEventsSearchCriteria;
	}

	public void setCompanyEventsSearchCriteria(CompanyEventsSearchCriteria companyEventsSearchCriteria) {
		this.companyEventsSearchCriteria = companyEventsSearchCriteria;
	}

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public Integer getActivePage() {
		return activePage;
	}
}
