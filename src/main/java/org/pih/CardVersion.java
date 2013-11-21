package org.pih;

public class CardVersion {

	private String id;
	private String card_id;
	private String number;
	private String version;
	private String name;
	private String description;
	private String card_type_name;
	private String comment;
	private String updated_at;
	private String modified_by_user_id;

	private String cp_date_raised; // Date the defect was raised (only present in defects and todos)
	private String cp_assigned_to_user_id; // User assigned to the card
	private String cp_start_date; // Date work started on the card

	private String cp_blocked_;  // If "Yes", then we should mark priority = Blocker.
	private String cp_defect_severity; // Severity of defect (not used very much, and only for type - defect)
	private String cp_status; // Current status of the card. (Work Status) eg. "In Development".  Not used in todos, which use Progress instead
	private String cp_progress; // Progress on to-do task.  Essentially same as status, just used for type = to-do
	private String cp_story_type; // Only used for type=story. can have value functional or tech
	private String cp_estimate; //  S, M, L, XL
	private String cp_qa_accepted_date; // Date accepted as ready to showcase

	private String cp_iteration_planned_card_id; // What is the planned iteration card id
	private String cp_release___release_card_id; // What is the planned release card id

	private String cp_defect_iteration___iteration_card_id; // For defects, What iteration is this defect assigned to (iteration card id)

	private String cp_story_task_card_id; // For tasks, if this task is a sub-task of another story, this references that story

	private String cp_toggle_status; // Done, Hide in production, Show in production
	private String cp_toggle_due_date; // Deadline to deliver a toggle  (not sure this is ever used)

	private String system_generated_comment; // Not sure whether this is used or not

	/**
	 * Not taking:
	 * cp_duration:  all seem to be negative values and sporadic?
	 * cp_release___task_card_id:  "Release - Task" is the name of the field.  what is this?
	 * created_at:  On needed on card I think
	 * redcloth:  Something for ruby regarding mark-up language
	 * updater_id: what does this represent?  Assuming this is handled by the modified by field?
	 */

	public CardVersion() {}

	public String toString() {
		return id + " (card " + card_id + ", #" + number + " v" + version + ")";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getModified_by_user_id() {
		return modified_by_user_id;
	}

	public void setModified_by_user_id(String modified_by_user_id) {
		this.modified_by_user_id = modified_by_user_id;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getCard_type_name() {
		return card_type_name;
	}

	public void setCard_type_name(String card_type_name) {
		this.card_type_name = card_type_name;
	}

	public String getCp_date_raised() {
		return cp_date_raised;
	}

	public void setCp_date_raised(String cp_date_raised) {
		this.cp_date_raised = cp_date_raised;
	}

	public String getCp_assigned_to_user_id() {
		return cp_assigned_to_user_id;
	}

	public void setCp_assigned_to_user_id(String cp_assigned_to_user_id) {
		this.cp_assigned_to_user_id = cp_assigned_to_user_id;
	}

	public String getCp_start_date() {
		return cp_start_date;
	}

	public void setCp_start_date(String cp_start_date) {
		this.cp_start_date = cp_start_date;
	}

	public String getCp_blocked_() {
		return cp_blocked_;
	}

	public void setCp_blocked_(String cp_blocked_) {
		this.cp_blocked_ = cp_blocked_;
	}

	public String getCp_defect_severity() {
		return cp_defect_severity;
	}

	public void setCp_defect_severity(String cp_defect_severity) {
		this.cp_defect_severity = cp_defect_severity;
	}

	public String getCp_status() {
		return cp_status;
	}

	public void setCp_status(String cp_status) {
		this.cp_status = cp_status;
	}

	public String getCp_progress() {
		return cp_progress;
	}

	public void setCp_progress(String cp_progress) {
		this.cp_progress = cp_progress;
	}

	public String getCp_story_type() {
		return cp_story_type;
	}

	public void setCp_story_type(String cp_story_type) {
		this.cp_story_type = cp_story_type;
	}

	public String getCp_estimate() {
		return cp_estimate;
	}

	public void setCp_estimate(String cp_estimate) {
		this.cp_estimate = cp_estimate;
	}

	public String getCp_qa_accepted_date() {
		return cp_qa_accepted_date;
	}

	public void setCp_qa_accepted_date(String cp_qa_accepted_date) {
		this.cp_qa_accepted_date = cp_qa_accepted_date;
	}

	public String getCp_iteration_planned_card_id() {
		return cp_iteration_planned_card_id;
	}

	public void setCp_iteration_planned_card_id(String cp_iteration_planned_card_id) {
		this.cp_iteration_planned_card_id = cp_iteration_planned_card_id;
	}

	public String getCp_release___release_card_id() {
		return cp_release___release_card_id;
	}

	public void setCp_release___release_card_id(String cp_release___release_card_id) {
		this.cp_release___release_card_id = cp_release___release_card_id;
	}

	public String getCp_defect_iteration___iteration_card_id() {
		return cp_defect_iteration___iteration_card_id;
	}

	public void setCp_defect_iteration___iteration_card_id(String cp_defect_iteration___iteration_card_id) {
		this.cp_defect_iteration___iteration_card_id = cp_defect_iteration___iteration_card_id;
	}

	public String getCp_story_task_card_id() {
		return cp_story_task_card_id;
	}

	public void setCp_story_task_card_id(String cp_story_task_card_id) {
		this.cp_story_task_card_id = cp_story_task_card_id;
	}

	public String getCp_toggle_status() {
		return cp_toggle_status;
	}

	public void setCp_toggle_status(String cp_toggle_status) {
		this.cp_toggle_status = cp_toggle_status;
	}

	public String getCp_toggle_due_date() {
		return cp_toggle_due_date;
	}

	public void setCp_toggle_due_date(String cp_toggle_due_date) {
		this.cp_toggle_due_date = cp_toggle_due_date;
	}

	public String getSystem_generated_comment() {
		return system_generated_comment;
	}

	public void setSystem_generated_comment(String system_generated_comment) {
		this.system_generated_comment = system_generated_comment;
	}
}
