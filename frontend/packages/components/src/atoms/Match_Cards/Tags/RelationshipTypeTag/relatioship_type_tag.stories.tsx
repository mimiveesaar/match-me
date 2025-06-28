import { RelationshipTypeTag } from "./relationship_type_tag";

export default {
  title: "Atoms/Match_Cards/Tags/RelationshipTypeTag",
  component: RelationshipTypeTag,
};

export const Default = () => (
  <RelationshipTypeTag relationshipType = "Astral companion" />
);