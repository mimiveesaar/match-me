import {
  IncomingPings,
  IncomingPingsProps,
} from "@organisms/Connections/IncomingPings/IncomingPings";
import {
  MyConnections,
  MyConnectionsProps,
} from "@organisms/Connections/MyConnections/MyConnections";
import {
  OutgoingPings,
  OutgoingPingsProps,
} from "@organisms/Connections/OutgoingPings/OutgoingPings";


export interface ConnectionsProps {
  incomingPings: IncomingPingsProps;
  outgoingPings: OutgoingPingsProps;
  myConnections: MyConnectionsProps;
}

export const Connections = ({
  incomingPings,
  outgoingPings,
  myConnections,
}: ConnectionsProps) => {
  return (
    <div className="mx-auto h-full max-sm:w-full">
      <div className="mt-20 flex h-full w-full flex-col justify-center sm:flex-row">
        <div className="flex w-full flex-col space-y-3 lg:flex-row lg:space-y-0 lg:p-3">
          <div className="flex items-start justify-center lg:ml-3">
            <MyConnections {...myConnections} />
          </div>
          <div className="flex flex-col items-center space-y-3 md:ml-2.5 md:flex-row md:space-y-0 md:space-x-2 lg:flex-col lg:space-y-3 lg:mt-1 lg:ml-8 lg:space-x-0">
            <IncomingPings {...incomingPings} />
            <OutgoingPings {...outgoingPings} />
          </div>
        </div>
      </div>
    </div>
  );
};
