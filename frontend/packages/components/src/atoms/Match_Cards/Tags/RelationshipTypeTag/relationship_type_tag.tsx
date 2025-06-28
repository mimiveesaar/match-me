import { Eye } from 'lucide-react';

type RelationshipTypeTagProps = {
  relationshipType: string;
  className?: string;
};

export function RelationshipTypeTag({ relationshipType, className = "" }: RelationshipTypeTagProps) {
  return (
    <span className={`h-17 inline-flex items-center gap-2 bg-ivory text-black text-xs font-medium font-noto_serif px-2 rounded-custom_4 ${className}`}>
       <Eye className="w-4 h-4 text-gray-700 align-middle relative" />
       {relationshipType}
    </span>
  );
}