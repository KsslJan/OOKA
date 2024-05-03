package org.hbrs.ooka.lzu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Lzu implements LzuPort {
//    private final Logger LOG = LoggerFactory.getLogger(Lzu.class);

    private final Map<String, Thread> threads = new HashMap<>();
    private final Map<String, Component> components = new HashMap<>();

    private static Lzu INSTANCE;

    private Lzu() {
    }

    public static Lzu getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Lzu();
        }
        return INSTANCE;
    }

    public void start() {
        int i = 0;
        while (true) {
            System.out.println("""
                    Command options:
                    (1)     Deploy component
                    (2)     Start component
                    (3)     Stop component
                    (4)     Undeploy component
                    (5)     Get component status
                    (6)     Stop lzu
                    """);

            int command = new Scanner(System.in).nextInt();
            switch (command) {
                case 1:
                    deployComponent("" + i);
                    break;
                case 2:
                    startComponent();
                    break;
                case 3:
                    stopComponent();
                    break;
                case 4:
                    removeComponent();
                    break;
                case 5:
                    printComponentStatus();
                    break;
                case 6:
                    stopLzu();
                    break;
            }
            i++;
        }
    }

    private void deployComponent(String id) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter absolute path to a jar file:");
        String pathToJar = scanner.nextLine();
        if (!Component.validateJarPath(pathToJar)) {
            return;
        }
        System.out.println("Enter name for component:");
        String componentName = scanner.nextLine();
        Component component = new Component(id, componentName, pathToJar);
        deploy(component);
        components.put(id, component);
    }

    private void deploy(Deployable d) {
        Thread thread = new Thread(() -> {
            if (d instanceof Component component)
                component.invokeStart();
        });
        d.setState(org.hbrs.ooka.component.state.State.DEPLOYED);
        threads.put(d.getId(), thread);
    }

    private void startComponent() {
        Component component = getComponent(List.of(org.hbrs.ooka.component.state.State.RUNNING));
        if (component != null) {
            System.out.println("Start component" + component.getName());
            component.start();
        }
    }

    private void stopComponent() {
        Component component = getComponent(List.of(org.hbrs.ooka.component.state.State.DEPLOYED, org.hbrs.ooka.component.state.State.STOPPED));
        if (component != null) {
            System.out.println("Stop component " + component.getName());
            component.stop();
            String s1 = threads.keySet().stream().filter(s -> s.equals(component.getId())).findFirst().get();
            Thread thread = threads.get(s1);
            thread.interrupt();
            deploy(component);
            component.setState(org.hbrs.ooka.component.state.State.STOPPED);
        }
    }

    private void removeComponent() {
        Component component = getComponent(List.of(org.hbrs.ooka.component.state.State.RUNNING));
        if (component != null) {
            System.out.println("Remove component " + component);
            threads.remove(component.getId());
            components.remove(component.getId());
        }
    }

    /**
     * Helper function to get component (to e.g. start, stop, undeploy)
     */
    private Component getComponent(List<org.hbrs.ooka.component.state.State> filterStates) {
        if (components.size() == 1 && filterStates.contains(components.values().iterator().next().getState())) {
            System.err.println("No component found to execute the selected operation on!");
            return null;
        }
        if (components.size() == 1) {
            return components.values().iterator().next();
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter id of component:");
        for (Component component : components.values()) {
            if (!filterStates.contains(component.getState())) {
                System.out.println(component);
            }
        }
        String componentId = scanner.nextLine();
        for (Component component : components.values()) {
            if (component.getId().equals(componentId)) {
                return component;
            }
        }
        System.err.println("Component with id " + componentId + " not found");
        return null;
    }

    private void printComponentStatus() {
        for (Component component : components.values()) {
            System.out.println(component);
        }
    }

    private void stopLzu() {
        System.out.println("Stopping Lzu");
        System.exit(0);
    }

    protected Thread getThread(String id) {
        return threads.get(id);
    }
}
