export type Job = {
  id: number;
  start: number;
  end: number;
}

export type Page<T> = {
  content: T[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  totalElements: number;
  totalPages: number;
};
