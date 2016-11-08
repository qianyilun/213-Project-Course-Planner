package ca.cmpt213.courseplanner.ui;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.cmpt213.bargraph.BarGraphIcon;
import ca.cmpt213.bargraph.BarGraphModel;
import ca.cmpt213.courseplanner.model.Course;
import ca.cmpt213.courseplanner.model.CourseOffering;
import ca.cmpt213.courseplanner.model.Model;
import ca.cmpt213.courseplanner.model.Semester;

/**
 * UI to display statistics for all the offerings of the currently selected course.
 */
@SuppressWarnings("serial")
public class StatisticsPanel extends TitledPanel {
	private static final int ICON_HEIGHT = 180;
	private static final int ICON_WIDTH = 250;
	
	private static final int HISTOGRAM_SPACING = 20;
	
	private static final int LOCATION_INDEX_BBY = 0;
	private static final int LOCATION_INDEX_SRY = 1;
	private static final int LOCATION_INDEX_VAN = 2;
	private static final int LOCATION_INDEX_OTHER = 3;
	
	private static final int SEMESTER_INDEX_SPRING = 0;
	private static final int SEMESTER_INDEX_SUMMER = 1;
	private static final int SEMESTER_INDEX_FALL = 2;
	private static final String[] SEMESTER_NAMES = {"Spring", "Summer", "Fall"};
	private static final String[] CAMPUS_NAMES = {"Bby", "Sry", "Van", "Other"};

	private BarGraphModel campusBarGraph;
	private BarGraphModel semesterBarGraph;
	private JLabel courseNameLabel;

	public StatisticsPanel(Model model) {
		super("Statistics", model);
		setMainContents(makeMainPanel());
	}
	private JComponent makeMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		panel.add(makeCourseNameLabel());
		panel.add(makeSemesterBarGraphPane());
		panel.add(makeLocationBarGraphPanel());

		allowOnlyHorizontalStretching(this);
		registerForUpdates();
		
		return panel;
	}
	private Component makeCourseNameLabel() {
		courseNameLabel = new JLabel("");
		return courseNameLabel;
	}
	private Component makeSemesterBarGraphPane() {
		int[] data = new int[SEMESTER_NAMES.length];
		semesterBarGraph = new BarGraphModel(data, SEMESTER_NAMES);
		BarGraphIcon icon = new BarGraphIcon(semesterBarGraph, ICON_WIDTH, ICON_HEIGHT);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(Box.createVerticalStrut(HISTOGRAM_SPACING));
		panel.add(new JLabel("Semester Offerings:"));
		panel.add(new JLabel(icon));

		return panel;
	}
	private Component makeLocationBarGraphPanel() {
		int[] data = new int[CAMPUS_NAMES.length];
		campusBarGraph = new BarGraphModel(data, CAMPUS_NAMES);
		BarGraphIcon icon = new BarGraphIcon(campusBarGraph, ICON_WIDTH, ICON_HEIGHT);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(Box.createVerticalStrut(HISTOGRAM_SPACING));
		panel.add(new JLabel("Campus Offerings:"));
		panel.add(new JLabel(icon));
		return panel;
	}
	
	
	private void registerForUpdates() {
		getModel().addSelectedCourseListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				update();
			}
		});
	}
	private void update() {
		Course selectedCourse = getModel().getSelectedCourse();
		
		if (selectedCourse == null) {
			clearData();
		} else {
			courseNameLabel.setText("Course: " + selectedCourse.toString());
			semesterBarGraph.setData(countOfferingsPerSemester(selectedCourse));
			campusBarGraph.setData(countOfferingsPerLocation(selectedCourse));
		}
		
		// Have Java redraw the area on the UI.
		updateUI();
	}

	private void clearData() {
		courseNameLabel.setText("Course: " + "");
		semesterBarGraph.setData(new int[SEMESTER_NAMES.length]);
		campusBarGraph.setData(new int[CAMPUS_NAMES.length]);		
	}

	private int[] countOfferingsPerLocation(Course course) {
		int data[] = new int[CAMPUS_NAMES.length];
		for (CourseOffering offering : course.offerings()) {
			String campus = offering.getLocation();
			int campusIdx = LOCATION_INDEX_OTHER;
			if (campus.equals("BURNABY")) {
				campusIdx = LOCATION_INDEX_BBY;
			} else if (campus.equals("SURREY")) {
				campusIdx = LOCATION_INDEX_SRY;
			} else if (campus.equals("HRBRCNTR")) {
				campusIdx = LOCATION_INDEX_VAN;
			}
			data[campusIdx]++;
		}
		
		return data;
	}

	private int[] countOfferingsPerSemester(Course course) {
		int data[] = new int[SEMESTER_NAMES.length];
		for (CourseOffering offering : course.offerings()) {
			Semester semester = offering.getSemester();

			int semesterIdx = 0;
			if (semester.isSpring()) {
				semesterIdx = SEMESTER_INDEX_SPRING;
			} else if (semester.isSummer()) {
				semesterIdx = SEMESTER_INDEX_SUMMER;
			} else if (semester.isFall()) {
				semesterIdx = SEMESTER_INDEX_FALL;
			} else {
				assert false;
			}
			data[semesterIdx]++;
		}
		
		return data;
	}
}


















