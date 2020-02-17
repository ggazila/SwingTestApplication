package swing.test.app;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Application {
    private static JFrame jFrame = new JFrame();


    public static void main(String[] args) {
        SwingUtilities.invokeLater(IntroScreen::new);
    }

    static class IntroScreen {
        private static final String INTRO_PAGE_TITLE = "Intro page";
        private static final String NUMBERS_TO_DISPLAY_LABEL = "How many numbers to display";
        private static final String ENTER_SUBMIT_BUTTON = "Enter";
        private static final int TEXTFIELD_SIZE = 10;
        private JTextField numberInput;
        private JButton submitButton;

        IntroScreen() {
            jFrame.setBounds(500, 200, 500, 500);
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            createIntroPage();
            executeSortScreen();
        }

        private void createIntroPage() {
            jFrame.setTitle(INTRO_PAGE_TITLE);

            Container container = jFrame.getContentPane();
            container.setLayout(new GridBagLayout());

            JLabel label = new JLabel(NUMBERS_TO_DISPLAY_LABEL);
            numberInput = new JTextField(TEXTFIELD_SIZE);
            submitButton = new JButton(ENTER_SUBMIT_BUTTON);
            submitButton.setBackground(Color.BLUE);
            submitButton.setForeground(Color.WHITE);
            submitButton.setPreferredSize(numberInput.getPreferredSize());

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(10, 0, 10, 0);
            constraints.gridy = 0;
            container.add(label, constraints);
            constraints.gridy++;
            container.add(numberInput, constraints);
            constraints.gridy++;
            container.add(submitButton, constraints);

            jFrame.setVisible(true);
        }

        private void executeSortScreen() {
            submitButton.addActionListener(e -> {
                int amountNumbersToShow = Integer.parseInt(numberInput.getText());
                if (amountNumbersToShow > 0 && amountNumbersToShow <= 1000) {
                    jFrame.getContentPane().removeAll();
                    jFrame.getContentPane().repaint();
                    new Sort(amountNumbersToShow);
                }
            });
        }
    }

    static class Sort {
        public static final String SORT_PAGE_TITLE = "Sort page";
        private static boolean sortType = true;
        private List<Integer> numbersToShow;
        private Integer numberOfButtons;

        Sort(int amountNumbersToShow) {
            numberOfButtons = amountNumbersToShow;

            numbersToShow = new Random()
                    .ints(numberOfButtons, 1, 1001)
                    .collect(ArrayList::new, List::add, List::addAll);
            numbersToShow.set(new Random().nextInt(numbersToShow.size()),
                    new Random().nextInt(30) + 1);

            createSortPage();
        }

        private void createSortPage() {
            Container container = jFrame.getContentPane();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();

            createNumbersView(container, gridBagConstraints);
            createControlButtons(container, gridBagConstraints);

            jFrame.setVisible(true);
        }

        private void createNumbersView(Container container, GridBagConstraints constraints) {
            constraints.weightx = 0.1;
            constraints.insets = new Insets(2, 0, 2, 0);
            jFrame.setTitle(SORT_PAGE_TITLE);

            int column = 0;
            for (int button = 0; button < numberOfButtons; button++) {
                JButton jButton = new JButton(String.valueOf(numbersToShow.get(button)));
                jButton.addActionListener(generateNewColumns(numbersToShow.get(button)));
                jButton.setBackground(Color.BLUE);
                jButton.setForeground(Color.WHITE);
                jButton.setPreferredSize(new Dimension(60, 30));

                constraints.gridy++;
                constraints.gridx = column;

                if (button % 10 == 9) {
                    constraints.gridy = -1;
                    column++;
                }
                container.add(jButton, constraints);
            }
        }

        private void createControlButtons(Container container,
                                          GridBagConstraints constraints) {
            constraints.gridy = 0;
            constraints.gridx++;
            JButton sortButton = new JButton("Sort");
            sortButton.setBackground(Color.GREEN);
            sortButton.setForeground(Color.WHITE);
            sortButton.setPreferredSize(new Dimension(80, 30));

            JButton returnButton = new JButton("Return");
            returnButton.setBackground(Color.GREEN);
            returnButton.setForeground(Color.WHITE);
            returnButton.setPreferredSize(new Dimension(80, 30));

            sortButton.addActionListener(sortNumbers());
            returnButton.addActionListener(returnToIntroPage());
            container.add(sortButton, constraints);
            constraints.gridy++;
            container.add(returnButton, constraints);
        }

        private ActionListener generateNewColumns(Integer number) {
            return e -> {
                if (number > 30) {
                    JOptionPane.showMessageDialog(jFrame, "Number " + number + " is bigger than "
                                                          + "30");
                } else {
                    jFrame.getContentPane().removeAll();
                    jFrame.getContentPane().repaint();
                    new Sort(number);
                }
            };
        }

        private ActionListener sortNumbers() {
            return e -> {
                if (sortType) {
                    Collections.sort(numbersToShow);
                    sortType = false;
                } else {
                    numbersToShow.sort(Collections.reverseOrder());
                    sortType = true;
                }
                new Sort(numberOfButtons, numbersToShow).refreshPage();
            };
        }

        private ActionListener returnToIntroPage() {
            return e -> {
                jFrame.getContentPane().removeAll();
                jFrame.getContentPane().repaint();
                new IntroScreen();
            };
        }

        private void refreshPage() {
            jFrame.getContentPane().removeAll();
            jFrame.getContentPane().repaint();
            new Sort(numberOfButtons, numbersToShow);
        }

        public Sort(Integer numberOfButtons, List<Integer> numbers) {
            this.numbersToShow = numbers;
            this.numberOfButtons = numberOfButtons;
            createSortPage();
        }
    }
}
