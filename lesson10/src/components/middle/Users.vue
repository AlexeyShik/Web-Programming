<template>
  <div class="users datatable">
    <div class="caption">User</div>
    <table>
      <thead>
      <tr>
        <th>Id</th>
        <th>Login</th>
        <th>Name</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :user="user" :key="user.id">
        <td class="id">{{ user.id }}</td>
        <td class="login">{{ user.login }}</td>
        <td class="name">{{ user.name }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>

export default {
  name: "Users",
  components: {

  },
  props: ["users"]
}
</script>

<style scoped>
.datatable table {
  width: 100%;
  text-align: center;
  border-collapse: collapse;
}

.datatable .caption {
  background-color: var(--datatable-color);
  font-size: 0.9rem;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  padding: 0.25rem 0.5rem;
}

.datatable td, .datatable th {
  border: 1px solid var(--datatable-color);
  padding: 0.5em;
  font-size: 0.8rem;
}

.datatable tbody tr:nth-child(2n-1) {
  background-color: var(--datatable-dark-cell-color);
}
</style>