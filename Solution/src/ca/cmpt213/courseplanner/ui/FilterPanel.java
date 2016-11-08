package ca.cmpt213.courseplanner.ui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.cmpt213.courseplanner.model.CourseFilter;
import ca.cmpt213.courseplanner.model.Department;
import ca.cmpt213.courseplanner.model.Model;
/**
 * UI to create a CourseFilter for selecting what courses to list.
 */
@SuppressWarnings("serial")
public class FilterPanel extends TitledPanel {
	private static final int SINGLE_COLUMN = 1;
	
	JComboBox<String> departmentComboBox;
	JCheckBox underGradCheckBox;
	JCheckBox gradCheckBox;

	public FilterPanel(Model model) {
		super("Course List Filter", model);
		setMainContents(makeMainPanel());
	}

	private JComponent makeMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,SINGLE_COLUMN));
		
		panel.add(makeDepartmentSelector());
		panel.add(makeUndergradCheckbox());
		panel.add(makeGradCheckbox());
		panel.add(makeUpdateButton());
		return panel;
	}

	private Component makeDepartmentSelector() {
		Vector<String> departments = new Vector<String>();
		for (Department department : getModel().departments()) {
			departments.add(department.toString());
		}
		departmentComboBox = new JComboBox<String>(departments);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(new JLabel("Department "));
		panel.add(departmentComboBox);
		return panel;
	}

	private Component makeUndergradCheckbox() {
		underGradCheckBox = new JCheckBox("Include undergrad courses", true);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(underGradCheckBox);
		return panel;
	}
	
	private Component makeGradCheckbox() {
		gradCheckBox = new JCheckBox("Include grad courses", false);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(gradCheckBox);
		return panel;
	}

	private Component makeUpdateButton() {
		JButton button = new JButton("Update Course List");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateFilter();
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(Box.createHorizontalGlue());
		panel.add(button);
		return panel;
	}

	private void updateFilter() {
		String department = (String) departmentComboBox.getSelectedItem();
		boolean includeUGrad = underGradCheckBox.isSelected();
		boolean includeGrad = gradCheckBox.isSelected();
		
		CourseFilter filter = new CourseFilter(department, includeUGrad, includeGrad);
		getModel().setCourseFilter(filter);
	}
}












