package hu.mta.sztaki.lpds.cloud.simulator.iaas.vmconsolidation.NN;


public class cloudConfigXML {
    public static void main(String args[]){
        int amount = 4;
        int machineID = 1;

        System.out.println(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<cloud id=\"test\"\tscheduler=\"hu.mta.sztaki.lpds.cloud.simulator.iaas.vmscheduling.RoundRobinScheduler\" \npmcontroller=\"hu.mta.sztaki.lpds.cloud.simulator.iaas.pmscheduling.SchedulingDependentMachines\">");
        for(int i=0;i<amount;i++) {
            System.out.println(
                    "\n<machine id=\"" + machineID + "\" cores=\"4\" processing=\"0.001\" memory=\"256000000000\">\n" +
                            "\t<powerstates kind=\"host\">\n" +
                            "\t\t<power\tmodel=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.LinearConsumptionModel\" idle=\"296\" max=\"493\" inState=\"default\" />\n" +
                            "\t\t<power\tmodel=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.ConstantConsumptionModel\" idle=\"20\" max=\"20\" inState=\"OFF\" />\n" +
                            "\t</powerstates>\n" +
                            "\t<statedelays startup=\"89000\" shutdown=\"29000\" />\n" +
                            "\t<repository id=\"" + machineID + "\" capacity=\"5000000000000\" inBW=\"250000\" outBW=\"250000\" diskBW=\"50000\">\n" +
                            "\t\t<powerstates kind=\"storage\">\n" +
                            "\t\t\t<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.LinearConsumptionModel\" idle=\"6.5\" max=\"9\" inState=\"default\" />\n" +
                            "\t\t\t<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.ConstantConsumptionModel\" idle=\"0\" max=\"0\" inState=\"OFF\" />\n" +
                            "\t\t</powerstates>\n" +
                            "\t\t<powerstates kind=\"network\">\n" +
                            "\t\t\t<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.LinearConsumptionModel\" idle=\"3.4\" max=\"3.8\" inState=\"default\" />\n" +
                            "\t\t\t<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.ConstantConsumptionModel\" idle=\"0\" max=\"0\" inState=\"OFF\" />\n" +
                            "\t\t</powerstates>");
            for(int j= 1;j<amount+1;j++){
                if(machineID != j) {
                    System.out.println(
                            "\t\t<latency towards=\"" + j + "\" value=\"5\" />");
                }
            }

            System.out.println(
                    "\t\t<latency towards=\"repo\" value=\"5\" />\n"+
                            "\t</repository>\n" +
                            "</machine>");
            machineID++;
        }

        System.out.println( "\t<repository id=\"repo\" capacity=\"38000000000000\" inBW=\"250000\" outBW=\"250000\" diskBW=\"100000\">\n" +
                "\t\t<powerstates kind=\"storage\">\n" +
                "\t\t\t<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.LinearConsumptionModel\" idle=\"65\" max=\"90\" inState=\"default\" />\n" +
                "\t\t\t<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.ConstantConsumptionModel\" idle=\"0\" max=\"0\" inState=\"OFF\" />\n" +
                "\t\t</powerstates>\n" +
                "\t\t<powerstates kind=\"network\">\n" +
                "\t\t\t<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.LinearConsumptionModel\" idle=\"3.4\" max=\"3.8\" inState=\"default\" />\n" +
                "\t\t\t<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.ConstantConsumptionModel\" idle=\"0\" max=\"0\" inState=\"OFF\" />\n" +
                "\t\t</powerstates>");
        for(int i=1;i<amount+1;i++){
            System.out.println("\t\t\t<latency towards=\""+i+"\" value=\"5\" />");
        }

        System.out.println( "\t</repository>\n" +
                "</cloud>");
    }
}
