"use client";


interface MessageHeaderProps {
  sender: string;
  time: string;      // e.g., "1:11 PM"
  date: string;      // e.g., "6/9/25"
}

export const MessageHeader = ({
  sender,
  time,
  date,
}: MessageHeaderProps) => {

  return (
    <div className={`flex justify-start text-black/70 chakra-petch text-[10px] pl-7 font-semibold`}>
      <div className="mr-1">
        <span>{sender}</span>
      </div>
      
      <span>{time} {date}</span>
    </div>
  );
};