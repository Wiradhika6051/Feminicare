export default interface User {
  id: string
  data: {
    last_name: string
    weight: number
    register_date: {
      _seconds: number
      _nanoseconds: number
    }
    first_name: string
    email: string
    date_of_birth: {
      _seconds: number
      _nanoseconds: number
    }
    username: string,
    salt:string,
  }
}
