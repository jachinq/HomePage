export interface User {
  id: number;
  username: string;
  password: string;
  role: string;
  avatar: string;
  enabled: boolean;
  createTime: string;
}