package ca.cmpt213.courseplanner.ui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.cmpt213.courseplanner.model.CourseOffering;
import ca.cmpt213.courseplanner.model.Model;
import ca.cmpt213.courseplanner.model.OfferingSection;

/**
 * UI to display the details of one selected offering.
 */
@SuppressWarnings("serial")
public class OfferingDetailsPanel extends TitledPanel {
	private static final int LABEL_WIDTH = 100;
	private static final int LINE_HEIGHT = 0;

	private static final int PANEL_PREF_HEIGHT = 150;
	private static final int NUM_ENROLMENT_COLUMNS = 2;
	
	JTextArea courseName;
	JTextArea location;
	JTextArea semester;
	JTextArea instructors;
	JPanel enrollmentGrid;

	public OfferingDetailsPanel(Model model) {
		super("Details of Course Offering", model);
		setMainContents(makeMainPanel());
		registerForOfferingSelectionUpdate();
	}
	private JComponent makeMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		courseName = makeAndAddSectionRow(panel, "Course Name:");
		semester = makeAndAddSectionRow(panel, "Semester:");
		location = makeAndAddSectionRow(panel, "Location:");
		instructors = makeAndAddSectionRow(panel, "Instructors:");

		enrollmentGrid = makeEnrollmentGrid();
		panel.add(enrollmentGrid); 
		panel.add(Box.createVerticalGlue());
		
		preventVerticalResizing(panel);
		return panel;
	}
	private JPanel makeEnrollmentGrid() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, NUM_ENROLMENT_COLUMNS));
		allowOnlyHorizontalStretching(panel);
		return panel;
	}

	private JTextArea makeAndAddSectionRow(JPanel panel, String string) {
		JLabel label = new JLabel(string);
		label.setPreferredSize(new Dimension(LABEL_WIDTH, LINE_HEIGHT));
		
		JTextArea text = new JTextArea();
		text.setEditable(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		
		JPanel rowPanel = new JPanel();
		rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.LINE_AXIS));
		rowPanel.add(label);
		rowPanel.add(text);
		allowOnlyHorizontalStretching(rowPanel);

		panel.add(rowPanel);
		return text;
	}

	private void preventVerticalResizing(JPanel panel) {
		panel.setPreferredSize(new Dimension(0, PANEL_PREF_HEIGHT));
	}

	private void registerForOfferingSelectionUpdate() {
		getModel().addSelectedOfferingListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				update();
			}
		});
	}

	private void update() {
		clearPanel();

		CourseOffering offering = getModel().getSelectedSection();
		if (offering != null) {
			courseName.setText(offering.getCourse().toString());
			location.setText(offering.getLocation());
			semester.setText(offering.getSemester().toString());
			instructors.setText(offering.getInstructors());
			
			populateEnrollmentGrid(offering);
		}
		updateUI();
	}
	private void clearPanel() {
		courseName.setText("");
		location.setText("");
		semester.setText("");
		instructors.setText("");
		enrollmentGrid.removeAll();
	}

	private void populateEnrollmentGrid(CourseOffering offering) {
		enrollmentGrid.removeAll();
		enrollmentGrid.add(new JLabel("Section Type"));
		enrollmentGrid.add(new JLabel("Enrollment (filled/cap)"));
		
		for (OfferingSection component : offering.components()) {
			String message = component.getEnrollmentTotal() + " / " + component.getEnrollmentCap();
			
			enrollmentGrid.add(new JLabel(component.getType()));
			enrollmentGrid.add(new JLabel(message));
		}
	}
}
